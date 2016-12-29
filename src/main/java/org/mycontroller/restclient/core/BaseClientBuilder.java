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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.Getter;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.0.0
 */

@Getter
public abstract class BaseClientBuilder<T> {

    private URI uri;
    private Optional<TRUST_HOST_TYPE> trustHostType = Optional.empty();
    private Optional<String> username = Optional.empty();
    private Optional<String> password = Optional.empty();
    private Map<String, Object> headers = new HashMap<>();
    private Map<String, Object> properties = new HashMap<>();

    public BaseClientBuilder<T> uri(String uri) throws URISyntaxException {
        return uri(new URI(uri), TRUST_HOST_TYPE.DEFAULT);
    }

    public BaseClientBuilder<T> uri(String uri, TRUST_HOST_TYPE trustHostType) throws URISyntaxException {
        return uri(new URI(uri), trustHostType);
    }

    public BaseClientBuilder<T> uri(URI uri) {
        return uri(uri, TRUST_HOST_TYPE.DEFAULT);
    }

    public BaseClientBuilder<T> uri(URI uri, TRUST_HOST_TYPE trustHostType) {
        this.uri = uri;
        this.trustHostType = Optional.ofNullable(trustHostType);
        return this;
    }

    public BaseClientBuilder<T> basicAuthentication(String username, String password) {
        this.username = Optional.ofNullable(username);
        this.password = Optional.ofNullable(password);
        return this;
    }

    public BaseClientBuilder<T> basicAuthentication(Optional<String> username, Optional<String> password) {
        this.username = username;
        this.password = password;
        return this;
    }

    public BaseClientBuilder<T> addHeader(String key, Object value) {
        headers.put(key, value);
        return this;
    }

    public BaseClientBuilder<T> addProperty(String key, Object value) {
        properties.put(key, value);
        return this;
    }

    public abstract T build();

}
