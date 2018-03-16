/*
 * Copyright 2015-2018 Jeeva Kandasamy (jkandasa@gmail.com)
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
package org.mycontroller.restclient.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.mycontroller.restclient.core.typeresolvers.CollectionJavaTypeResolver;
import org.mycontroller.restclient.core.typeresolvers.MapJavaTypeResolver;
import org.mycontroller.restclient.core.typeresolvers.SimpleJavaTypeResolver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.1.0
 */

@Slf4j
public class McHttpClient {

    // https://httpstatuses.com/
    public enum STATUS_CODE {
        OK(200),
        CREATED(201),
        ACCEPTED(202),
        NO_CONTENT(204),
        BAD_REQUEST(400),
        UNAUTHORIZED(401),
        FORBIDDEN(403),
        NOT_FOUND(404);

        int code;

        STATUS_CODE(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    private CloseableHttpClient client = null;
    private RequestConfig customRequestConfig = null;

    private ObjectMapper mapper = new ClientObjectMapper();

    private SimpleJavaTypeResolver simpleJavaTypeResolver = new SimpleJavaTypeResolver();
    private CollectionJavaTypeResolver collectionJavaTypeResolver = new CollectionJavaTypeResolver();
    private MapJavaTypeResolver mapJavaTypeResolver = new MapJavaTypeResolver();

    public McHttpClient() {
        client = HttpClientBuilder.create().build();
    }

    public McHttpClient(TRUST_HOST_TYPE trustHostType) {
        customRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.STANDARD)
                .build();
        if (trustHostType == TRUST_HOST_TYPE.ANY) {
            client = getHttpClientTrustAll();
        } else {
            client = getHttpClient();
        }
    }

    protected CloseableHttpClient getHttpClient() {
        return HttpClients.custom()
                .setDefaultRequestConfig(customRequestConfig)
                .build();
    }

    //trust any host
    private CloseableHttpClient getHttpClientTrustAll() {
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
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(),
                    new AnyHostnameVerifier());
            return HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .setDefaultRequestConfig(customRequestConfig)
                    .build();
        } catch (Exception ex) {
            _logger.error("Exception, ", ex);
            throw new RuntimeException("Unable to create trust ANY http client. Error: " + ex.getMessage());
        }
    }

    //Trust all hostname
    class AnyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }

    }

    private McHttpResponse execute(HttpUriRequest request) {
        CloseableHttpResponse response = null;
        try {
            response = client.execute(request);
            return McHttpResponse.get(request.getURI(), response);
        } catch (Exception ex) {
            _logger.debug("Exception,", ex);
            throw new RuntimeException(
                    MessageFormat.format("Failed to execute, Request:{0}, error:{1}", request, ex.getMessage()));
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ex) {
                    _logger.error("Exception,", ex);
                }
            }
        }
    }

    private URI getURI(String url, Map<String, Object> queryParameters) throws URISyntaxException {
        if (queryParameters != null && !queryParameters.isEmpty()) {
            List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
            for (String key : queryParameters.keySet()) {
                queryParams.add(new BasicNameValuePair(key, String.valueOf(queryParameters.get(key))));
            }
            return new URIBuilder(url).addParameters(queryParams).build();
        } else {
            return new URIBuilder(url).build();
        }
    }

    // GET, POST, PUT, DELETE methods start

    // HTTP DELETE request
    protected McHttpResponse doDelete(String url, Integer expectedResponseCode) {
        return doDelete(url, McHeader.getDefault(), expectedResponseCode);
    }

    // HTTP DELETE request - primary method
    protected McHttpResponse doDelete(String url, McHeader header, Integer expectedResponseCode) {
        HttpDelete delete = new HttpDelete(url);
        header.updateHeaders(delete);
        McHttpResponse httpResponse = execute(delete);
        // validate response
        validateResponse(httpResponse, expectedResponseCode);
        return httpResponse;
    }

    // HTTP GET request
    protected McHttpResponse doGet(String url, Integer expectedResponseCode) {
        return doGet(url, null, McHeader.getDefault(), expectedResponseCode);
    }

    // HTTP GET request
    protected McHttpResponse doGet(String url, Map<String, Object> queryParameters, Integer expectedResponseCode) {
        return doGet(url, queryParameters, McHeader.getDefault(), expectedResponseCode);
    }

    // HTTP GET request - primary method
    protected McHttpResponse doGet(String url, Map<String, Object> queryParameters,
            McHeader header, Integer expectedResponseCode) {
        try {
            HttpGet get = new HttpGet(getURI(url, queryParameters));
            header.updateHeaders(get);
            // execute
            McHttpResponse httpResponse = execute(get);
            // validate response
            validateResponse(httpResponse, expectedResponseCode);
            return httpResponse;
        } catch (Exception ex) {
            _logger.error("Exception when calling url:[{}], headers:[{}]", url, header, ex);
            throw new RuntimeException(
                    MessageFormat.format("Failed to execute, url:{0}, error:{1}", url, ex.getMessage()));
        }
    }

    // HTTP GET request
    protected McHttpResponse doGet(String url, McHeader header, Integer expectedResponseCode) {
        return doGet(url, null, header, expectedResponseCode);
    }

    // HTTP POST request - primary method
    protected McHttpResponse doPost(String url, Map<String, Object> queryParameters,
            McHeader header, HttpEntity entity, Integer expectedResponseCode) {
        try {
            HttpPost post = new HttpPost(getURI(url, queryParameters));
            header.updateHeaders(post);
            post.setEntity(entity);
            // execute
            McHttpResponse httpResponse = execute(post);
            // validate response
            validateResponse(httpResponse, expectedResponseCode);
            return httpResponse;
        } catch (Exception ex) {
            _logger.error("Exception when calling url:[{}], headers:[{}], queryParameters:[{}]",
                    url, header, queryParameters, ex);
            throw new RuntimeException(
                    MessageFormat.format("Failed to execute, url:{0}, error:{1}", url, ex.getMessage()));
        }
    }

    // HTTP POST request
    protected McHttpResponse doPost(String url, Map<String, Object> queryParameters,
            McHeader header, Integer expectedResponseCode) {
        StringEntity stringEntity = null;
        return doPost(url, queryParameters, header, stringEntity, expectedResponseCode);
    }

    // HTTP POST request
    protected McHttpResponse doPost(String url, Map<String, Object> queryParameters,
            McHeader header, String entity, Integer expectedResponseCode) {
        _logger.debug("Entity: {}", entity);
        try {
            return doPost(url, queryParameters, header, new StringEntity(entity), expectedResponseCode);
        } catch (UnsupportedEncodingException ex) {
            _logger.error("Exception when calling url:[{}], queryParameters:[{}], headers:[{}], entity:[{}]",
                    url, queryParameters, header, entity, ex);
            throw new RuntimeException(
                    MessageFormat.format("Failed to execute, url:{0}, error:{1}", url, ex.getMessage()));
        }
    }

    // HTTP POST request
    protected McHttpResponse doPost(String url, McHeader header, String entity,
            Integer expectedResponseCode) {
        return doPost(url, null, header, entity, expectedResponseCode);
    }

    // HTTP PUT request
    protected McHttpResponse doPut(String url, McHeader header, HttpEntity entity, Integer expectedResponseCode) {
        HttpPut put = new HttpPut(url);
        header.updateHeaders(put);
        put.setEntity(entity);
        // execute
        McHttpResponse httpResponse = execute(put);
        // validate response
        validateResponse(httpResponse, expectedResponseCode);
        return httpResponse;
    }

    // HTTP PUT request
    protected McHttpResponse doPut(String url, McHeader header, Integer expectedResponseCode) {
        StringEntity stringEntity = null;
        return doPut(url, header, stringEntity, expectedResponseCode);
    }

    // HTTP PUT request
    protected McHttpResponse doPut(String url, McHeader header, String entity, Integer expectedResponseCode) {
        _logger.debug("Entity: {}", entity);
        try {
            return doPut(url, header, new StringEntity(entity), expectedResponseCode);
        } catch (UnsupportedEncodingException ex) {
            _logger.error("Exception when calling url:[{}], headers:[{}], entity:[{}]", url, header, entity, ex);
            throw new RuntimeException(
                    MessageFormat.format("Failed to execute, url:{0}, error:{1}", url, ex.getMessage()));
        }
    }

    public String getHeader(McHttpResponse response, String name) {
        Header[] headers = response.getHeaders();
        for (int index = 0; index < headers.length; index++) {
            Header header = headers[index];
            if (header.getName().equalsIgnoreCase(name)) {
                return header.getValue();
            }
        }
        return null;
    }

    public void updateOnNull(Map<String, Object> data, String key, Object value) {
        if (data.get(key) == null) {
            data.put(key, value);
        }
    }

    // validate response
    public void validateResponse(McHttpResponse response, Integer expectedResponseCode) {
        _logger.debug("{}", response);
        if (expectedResponseCode != null) {
            if (!response.getResponseCode().equals(expectedResponseCode)) {
                throw new RuntimeException("Did not match with expected response code[" + expectedResponseCode + "], "
                        + response.toString());
            }
        }
    }

    public SimpleJavaTypeResolver simpleResolver() {
        return simpleJavaTypeResolver;
    }

    public CollectionJavaTypeResolver collectionResolver() {
        return collectionJavaTypeResolver;
    }

    public MapJavaTypeResolver mapResolver() {
        return mapJavaTypeResolver;
    }

    public Object readValue(String entity, JavaType javaType) {
        try {
            return mapper.readValue(entity, javaType);
        } catch (IOException ex) {
            _logger.error("Exception: entity[{}]", entity, ex);
            throw new RuntimeException("Exception: " + ex.getMessage());
        }
    }

    public String toJsonString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            _logger.error("Exception,", ex);
            throw new RuntimeException("Exception: " + ex.getMessage());
        }
    }

}