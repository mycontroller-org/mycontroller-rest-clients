/*
 * Copyright 2015-2017 Jeeva Kandasamy (jkandasa@gmail.com)
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
package org.mycontroller.restclient.core.jaxrs;

import javax.ws.rs.client.WebTarget;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.jboss.resteasy.client.jaxrs.ProxyBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.mycontroller.restclient.core.ClientInfo;
import org.mycontroller.restclient.core.TRUST_HOST_TYPE;
import org.mycontroller.restclient.core.jaxrs.fasterxml.jackson.JacksonObjectMapperProvider;
import org.mycontroller.restclient.core.jaxrs.fasterxml.jackson.MCJacksonJson2Provider;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.0.0
 */

public class RestFactory<T> {

    private final ClassLoader classLoader;
    private Class<T> apiClassType;
    private ResteasyClient client = null;

    public RestFactory(Class<T> clz) {
        classLoader = null;
        apiClassType = clz;
    }

    public RestFactory(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    private ResteasyClient getResteasyClient(ClientInfo clientInfo) {
        if (client == null) {
            final HttpClient httpclient;
            McHttpClient mcHttpClient = new McHttpClient();
            if (clientInfo.getEndpointUri().toString().startsWith("https")
                    && clientInfo.getTrustHostType() == TRUST_HOST_TYPE.ANY) {
                httpclient = mcHttpClient.getHttpClientTrustAll();
            } else {
                httpclient = mcHttpClient.getHttpClient();
            }

            ApacheHttpClient4Engine engine = null;
            if (clientInfo.getUsername().isPresent() && clientInfo.getPassword().isPresent()) {
                HttpHost targetHost = new HttpHost(clientInfo.getEndpointUri().getHost(), clientInfo.getEndpointUri()
                        .getPort());
                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider
                        .setCredentials(
                                new AuthScope(targetHost.getHostName(), targetHost.getPort()),
                                new UsernamePasswordCredentials(clientInfo.getUsername().get(), clientInfo
                                        .getPassword().get()));
                // Create AuthCache instance
                AuthCache authCache = new BasicAuthCache();
                // Generate BASIC scheme object and add it to the local auth cache
                BasicScheme basicAuth = new BasicScheme();
                authCache.put(targetHost, basicAuth);
                // Add AuthCache to the execution context
                HttpClientContext context = HttpClientContext.create();
                context.setCredentialsProvider(credsProvider);
                context.setAuthCache(authCache);
                engine = new ApacheHttpClient4Engine(httpclient, context);
            } else {
                engine = new ApacheHttpClient4Engine(httpclient);
            }
            client = new ResteasyClientBuilder().httpEngine(engine).build();
            client.register(JacksonJaxbJsonProvider.class);
            client.register(JacksonObjectMapperProvider.class);
            client.register(RestRequestFilter.class);
            client.register(new RequestHeadersFilter(clientInfo.getHeaders()));
            client.register(RestResponseFilter.class);
            client.register(MCJacksonJson2Provider.class);
        }
        return client;
    }

    public WebTarget webTarget(ClientInfo clientInfo) {
        return getResteasyClient(clientInfo).target(clientInfo.getEndpointUri());
    }

    public T createAPI(ClientInfo clientInfo) {
        ProxyBuilder<T> proxyBuilder = getResteasyClient(clientInfo).target(clientInfo.getEndpointUri()).proxyBuilder(
                apiClassType);
        if (classLoader != null) {
            proxyBuilder = proxyBuilder.classloader(classLoader);
        }
        return proxyBuilder.build();
    }
}