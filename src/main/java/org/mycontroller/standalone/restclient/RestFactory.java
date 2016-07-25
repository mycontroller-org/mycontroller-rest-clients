/*
 * Copyright 2015-2016 Jeeva Kandasamy (jkandasa@gmail.com)
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mycontroller.standalone.restclient;

import java.net.URI;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.jboss.resteasy.client.jaxrs.ProxyBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 0.0.3
 */
@Slf4j
public class RestFactory<T> {
    private HashMap<String, Object> headers = null;
    private final ClassLoader classLoader;
    private Class<T> proxyClazz;

    public RestFactory(Class<T> clazz) {
        classLoader = null;
        proxyClazz = clazz;
    }

    public enum TRUST_HOST_TYPE {
        DEFAULT("Default"),
        ANY("Any");
        public static TRUST_HOST_TYPE get(int id) {
            for (TRUST_HOST_TYPE type : values()) {
                if (type.ordinal() == id) {
                    return type;
                }
            }
            throw new IllegalArgumentException(String.valueOf(id));
        }

        private String value;

        private TRUST_HOST_TYPE(String value) {
            this.value = value;
        }

        public String getText() {
            return this.value;
        }

        public static TRUST_HOST_TYPE fromString(String text) {
            if (text != null) {
                for (TRUST_HOST_TYPE type : TRUST_HOST_TYPE.values()) {
                    if (text.equalsIgnoreCase(type.getText())) {
                        return type;
                    }
                }
            }
            return null;
        }
    }

    public T createAPI(URI uri, String userName, String password, TRUST_HOST_TYPE trustHostType) {
        HttpClient httpclient = null;
        if (uri.toString().startsWith("https") && trustHostType == TRUST_HOST_TYPE.ANY) {
            httpclient = getHttpClient();
        } else {
            httpclient = HttpClientBuilder.create().build();
        }
        ResteasyClient client = null;
        if (userName != null) {
            HttpHost targetHost = new HttpHost(uri.getHost(), uri.getPort());
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(
                    new AuthScope(targetHost.getHostName(), targetHost.getPort()),
                    new UsernamePasswordCredentials(userName, password));
            // Create AuthCache instance
            AuthCache authCache = new BasicAuthCache();
            // Generate BASIC scheme object and add it to the local auth cache
            BasicScheme basicAuth = new BasicScheme();
            authCache.put(targetHost, basicAuth);
            // Add AuthCache to the execution context
            HttpClientContext context = HttpClientContext.create();
            context.setCredentialsProvider(credsProvider);
            context.setAuthCache(authCache);
            ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(httpclient, context);

            client = new ResteasyClientBuilder().httpEngine(engine).build();
        } else {
            ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(getHttpClient());
            client = new ResteasyClientBuilder().httpEngine(engine).build();
        }

        client.register(JacksonJaxbJsonProvider.class);
        client.register(JacksonObjectMapperProvider.class);
        client.register(new RestRequestFilter(headers));
        client.register(RestResponseFilter.class);
        client.register(MCJacksonJson2Provider.class);
        ProxyBuilder<T> proxyBuilder = client.target(uri).proxyBuilder(proxyClazz);
        if (classLoader != null) {
            proxyBuilder = proxyBuilder.classloader(classLoader);
        }
        return proxyBuilder.build();
    }

    //trust any host
    private HttpClient getHttpClient() {
        SSLContextBuilder builder = new SSLContextBuilder();
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            builder.loadTrustMaterial(keyStore, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] trustedCert, String nameConstraints)
                        throws CertificateException {
                    return true;
                }
            });
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(
                    sslsf).build();
            return httpclient;

        } catch (Exception ex) {
            _logger.error("Exception, ", ex);
            return null;
        }
    }

    public void setHeaders(HashMap<String, Object> headers) {
        this.headers = headers;
    }
}