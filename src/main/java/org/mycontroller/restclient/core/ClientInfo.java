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

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.0.0
 */

@Getter
@ToString
public final class ClientInfo {

    private final URI endpointUri;
    private final Optional<String> username;
    private final Optional<String> password;
    private final Map<String, Object> headers;
    private final Map<String, Object> properties;
    private final TRUST_HOST_TYPE trustHostType;

    public ClientInfo(URI endpointUri, Optional<TRUST_HOST_TYPE> trustHostType, Optional<String> username,
            Optional<String> password, Map<String, Object> headers, Map<String, Object> properties) {
        this.endpointUri = checkNotNull(endpointUri);
        this.trustHostType = trustHostType.orElse(TRUST_HOST_TYPE.DEFAULT);
        this.username = checkNotNull(username);
        this.password = checkNotNull(password);
        this.headers = ImmutableMap.copyOf(checkNotNull(headers));
        this.properties = ImmutableMap.copyOf(checkNotNull(properties));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ClientInfo that = (ClientInfo) o;

        if (endpointUri != null ? !endpointUri.equals(that.endpointUri) : that.endpointUri != null)
            return false;
        if (trustHostType != that.trustHostType)
            return false;
        if (username != null ? !username.equals(that.username) : that.username != null)
            return false;
        if (password != null ? !password.equals(that.password) : that.password != null)
            return false;
        return headers != null ? headers.equals(that.headers) : that.headers == null;

    }

    @Override
    public int hashCode() {
        int result = endpointUri != null ? endpointUri.hashCode() : 0;
        result = 31 * result + trustHostType.getText().hashCode();
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        return result;
    }

}