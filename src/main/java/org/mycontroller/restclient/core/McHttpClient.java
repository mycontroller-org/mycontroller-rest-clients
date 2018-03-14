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

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
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
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.1.0
 */

@Slf4j
public class McHttpClient {

    //Trust all hostname
    class AnyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }

    }

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

    private HttpClient client = null;

    protected Gson gson = new Gson();

    public McHttpClient() {
        client = HttpClientBuilder.create().build();
    }

    public McHttpClient(TRUST_HOST_TYPE trustHostType) {
        if (trustHostType == TRUST_HOST_TYPE.ANY) {
            client = getHttpClientTrustAll();
        } else {
            client = getHttpClient();
        }
    }

    // GET, POST, PUT, DELETE methods start

    // HTTP DELETE request
    protected McHttpResponse doDelete(String url, Integer expectedResponseCode) {
        return doDelete(url, McHeader.getDefault(), expectedResponseCode);
    }

    // HTTP DELETE request - primary method
    protected McHttpResponse doDelete(String url, McHeader header, Integer expectedResponseCode) {
        try {
            HttpDelete delete = new HttpDelete(url);
            header.updateHeaders(delete);
            HttpResponse response = client.execute(delete);
            McHttpResponse httpResponse = McHttpResponse.get(delete.getURI(), response);
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
            HttpResponse response = client.execute(get);
            McHttpResponse httpResponse = McHttpResponse.get(get.getURI(), response);
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
            HttpResponse response = client.execute(post);
            McHttpResponse httpResponse = McHttpResponse.get(post.getURI(), response);
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
        try {
            HttpPut put = new HttpPut(url);
            header.updateHeaders(put);
            put.setEntity(entity);
            HttpResponse response = client.execute(put);
            McHttpResponse httpResponse = McHttpResponse.get(put.getURI(), response);
            // validate response
            validateResponse(httpResponse, expectedResponseCode);
            return httpResponse;

        } catch (Exception ex) {
            _logger.error("Exception when calling url:[{}], headers:[{}]", url, header, ex);
            throw new RuntimeException(
                    MessageFormat.format("Failed to execute, url:{0}, error:{1}", url, ex.getMessage()));
        }
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

    protected String getHeader(McHttpResponse response, String name) {
        Header[] headers = response.getHeaders();
        for (int index = 0; index < headers.length; index++) {
            Header header = headers[index];
            if (header.getName().equalsIgnoreCase(name)) {
                return header.getValue();
            }
        }
        return null;
    }

    protected HttpClient getHttpClient() {
        return HttpClientBuilder.create().build();
    }

    //trust any host
    private HttpClient getHttpClientTrustAll() {
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
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            return httpclient;
        } catch (Exception ex) {
            _logger.error("Exception, ", ex);
            return null;
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

    protected Type listOfMapType() {
        return new TypeToken<List<Map<String, Object>>>() {
        }.getType();
    }

    protected Type listType(Class<?> cls) {
        return TypeToken.getParameterized(List.class, cls).getType();
    }

    protected Type mapType(Class<?> clsKey, Class<?> clsValue) {
        return TypeToken.getParameterized(Map.class, clsKey, clsValue).getType();
    }

    protected String toJsonString(Object object) {
        return gson.toJson(object);
    }

    protected void updateOnNull(Map<String, Object> data, String key, Object value) {
        if (data.get(key) == null) {
            data.put(key, value);
        }
    }

    // validate response
    protected void validateResponse(McHttpResponse response, Integer expectedResponseCode) {
        _logger.debug("{}", response);
        if (expectedResponseCode != null) {
            if (!response.getResponseCode().equals(expectedResponseCode)) {
                throw new RuntimeException("Did not match with expected response code[" + expectedResponseCode + "], "
                        + response.toString());
            }
        }
    }
}