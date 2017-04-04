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
package org.mycontroller.standalone.restclient;

import java.net.URI;
import java.util.HashMap;

import org.mycontroller.restclient.core.TRUST_HOST_TYPE;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 0.0.3
 */
public class ClientBase<T> {
    private T restApi;
    private URI targetUri;
    private HashMap<String, Object> headers = null;

    public ClientBase(URI targetUri, String username, String password, TRUST_HOST_TYPE trustHostType,
            RestFactory<T> restFactory, HashMap<String, Object> headers) throws Exception {
        if (headers == null) {
            headers = new HashMap<String, Object>();
        }
        this.headers = headers;
        this.targetUri = targetUri;
        restFactory.setHeaders(this.headers);
        restApi = (T) restFactory.createAPI(targetUri, username, password, trustHostType);
    }

    public ClientBase(URI targetUri, String username, String password,
            RestFactory<T> restFactory, TRUST_HOST_TYPE trustHostType) throws Exception {
        this(targetUri, username, password, trustHostType, restFactory, null);
    }

    public ClientBase(URI targetUri, RestFactory<T> restFactory, TRUST_HOST_TYPE trustHostType) throws Exception {
        this(targetUri, null, null, trustHostType, restFactory, null);
    }

    public ClientBase(URI targetUri, RestFactory<T> restFactory, TRUST_HOST_TYPE trustHostType,
            HashMap<String, Object> headers) throws Exception {
        this(targetUri, null, null, trustHostType, restFactory, headers);
    }

    public T restApi() {
        return this.restApi;
    }

    public void updateHeader(String key, Object value) {
        headers.put(key, value);
    }

    public void removeHeader(String key) {
        headers.remove(key);
    }

    public URI getTargetUri() {
        return targetUri;
    }
}