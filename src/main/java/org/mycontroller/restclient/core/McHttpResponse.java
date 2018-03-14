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
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.mycontroller.restclient.core.McHttpClient.STATUS_CODE;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.1.0
 */

@Getter
@ToString
@Builder
@Slf4j
public class McHttpResponse {
    public static McHttpResponse get(URI uri, HttpResponse response)
            throws UnsupportedOperationException, IOException {
        _logger.debug("{}", response);
        McHttpResponse mcResponse = McHttpResponse.builder()
                .uri(uri)
                .responseCode(response.getStatusLine().getStatusCode())
                .headers(response.getAllHeaders())
                .build();
        if (response.getStatusLine().getStatusCode() != STATUS_CODE.NO_CONTENT.getCode()) {
            mcResponse.entity = IOUtils.toString(response.getEntity().getContent());
        }
        return mcResponse;
    }

    private URI uri;
    private String entity;
    private Integer responseCode;
    private Header[] headers;

    private String exception;
}