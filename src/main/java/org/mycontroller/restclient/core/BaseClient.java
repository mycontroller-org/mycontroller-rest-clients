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

import org.mycontroller.restclient.core.jaxrs.RestFactory;
import org.mycontroller.restclient.core.typeresolvers.CollectionJavaTypeResolver;
import org.mycontroller.restclient.core.typeresolvers.MapJavaTypeResolver;
import org.mycontroller.restclient.core.typeresolvers.SimpleJavaTypeResolver;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.0.0
 */

public abstract class BaseClient<T> {

    private T restAPI;
    private SimpleJavaTypeResolver simpleJavaTypeResolver = new SimpleJavaTypeResolver();
    private CollectionJavaTypeResolver collectionJavaTypeResolver = new CollectionJavaTypeResolver();
    private MapJavaTypeResolver mapJavaTypeResolver = new MapJavaTypeResolver();

    public BaseClient(ClientInfo clientInfo, RestFactory<T> restFactory) {
        checkNotNull(clientInfo);
        restAPI = restFactory.createAPI(clientInfo);
    }

    public T restApi() {
        return this.restAPI;
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
}