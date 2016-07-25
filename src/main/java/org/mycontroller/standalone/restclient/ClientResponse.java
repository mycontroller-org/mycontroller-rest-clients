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

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 0.0.3
 */
@Data
@ToString
@Slf4j
public class ClientResponse<T> {
    private int statusCode;
    private String errorMsg;
    private T entity;
    private boolean success = false;

    public ClientResponse(Class<?> clazz, Response response, int statusCode, boolean isEntityList) {
        this(clazz, response, statusCode, isEntityList == true ? List.class : null);
    }

    public ClientResponse(Class<?> clazz, Response response, int statusCode) {
        this(clazz, response, statusCode, null);
    }

    public ClientResponse(Response response, int statusCode) {
        this(null, response, statusCode, null);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ClientResponse(
            Class<?> clazz, Response response,
            int statusCode,
            Class<? extends Collection> collectionType) {
        try {
            this.setStatusCode(response.getStatus());
            if (response.getStatus() == statusCode) {
                this.setSuccess(true);
                ObjectMapper objectMapper = new ObjectMapper();
                if (collectionType != null) {
                    this.setEntity(objectMapper.readValue(response.readEntity(String.class),
                            objectMapper.getTypeFactory().constructCollectionType(collectionType, clazz)));
                } else if (clazz != null) {
                    this.setEntity((T) objectMapper.readValue(response.readEntity(String.class), clazz));
                } else {
                    this.setEntity((T) response.readEntity(String.class));
                }
            } else {
                this.setErrorMsg(response.readEntity(String.class));
            }
        } catch (JsonParseException ex) {
            _logger.error("Error, ", ex);
        } catch (JsonMappingException ex) {
            _logger.error("Error, ", ex);
        } catch (IOException ex) {
            _logger.error("Error, ", ex);
        } finally {
            response.close();
        }
    }

}
