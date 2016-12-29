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
package org.mycontroller.restclient.core;

import java.io.IOException;
import java.util.Arrays;

import javax.ws.rs.core.Response;

import org.mycontroller.restclient.core.jaxrs.Empty;
import org.mycontroller.restclient.core.jaxrs.ResponseCodes;
import org.mycontroller.restclient.core.jaxrs.fasterxml.jackson.ClientObjectMapper;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.0.0
 */

@Slf4j
public class DefaultClientResponse<T> implements ClientResponse<T> {

    private int statusCode;
    private String errorMsg;
    private T entity;
    private boolean isSuccess = false;
    private String rawEntity;

    private final JavaType javaType;
    private final ResponseCodes expectedCode;

    public DefaultClientResponse(JavaType javaType, Response serverResponse, ResponseCodes expectedCode) {
        this.javaType = javaType;
        this.expectedCode = expectedCode;

        validateResponse(serverResponse, expectedCode);
    }

    private void validateResponse(Response serverResponse, ResponseCodes expectedCode) {
        try {
            setStatusCode(serverResponse.getStatus());
            setSuccess(expectedCode.value() == serverResponse.getStatus());
            setRawEntity(serverResponse.readEntity(String.class));
        } finally {
            if (_logger.isDebugEnabled()) {
                _logger.debug("Client Response: {}", toString());
            }
        }
    }

    private void parse() {
        if (isSuccess()) {
            if (!javaType.getRawClass().isAssignableFrom(Empty.class)) {
                try {
                    ObjectMapper objectMapper = new ClientObjectMapper();
                    setEntity(objectMapper.readValue(getRawEntity(), javaType));
                } catch (IOException ex) {
                    _logger.error("Failed to parse: {}", ex);

                    setErrorMsg("Failed to parse: " + ex.toString());
                    setSuccess(Boolean.FALSE);
                }
            }
        } else {
            setErrorMsg("Expected code '" + expectedCode.toString() + "' but got '" + getStatusCode() + "'");
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        this.isSuccess = success;
    }

    public T getEntity() {
        if (entity == null) {
            parse();
        }

        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getRawEntity() {
        return rawEntity;
    }

    public void setRawEntity(String rawEntity) {
        this.rawEntity = rawEntity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Status Code:").append(getStatusCode())
                .append(", Is Success:").append(isSuccess())
                .append(", Error Message:").append(getErrorMsg() == null ? "-" : getErrorMsg())
                .append(", Raw Entity:").append(getRawEntity());

        if (getEntity() instanceof Object[]) {
            builder.append(", Entity:").append(Arrays.toString((T[]) getEntity()));
        } else {
            builder.append(", Entity:[").append(getEntity()).append("]");
        }

        return builder.toString();
    }
}