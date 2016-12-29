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
package org.mycontroller.restclient.philips.hue.clients;

import java.util.Map;

import javax.ws.rs.core.Response;

import org.mycontroller.restclient.core.BaseClient;
import org.mycontroller.restclient.core.ClientInfo;
import org.mycontroller.restclient.core.ClientResponse;
import org.mycontroller.restclient.core.DefaultClientResponse;
import org.mycontroller.restclient.core.jaxrs.Empty;
import org.mycontroller.restclient.core.jaxrs.ResponseCodes;
import org.mycontroller.restclient.core.jaxrs.RestFactory;
import org.mycontroller.restclient.philips.hue.PhilipsHueClient;
import org.mycontroller.restclient.philips.hue.jaxrs.handlers.LightsHandler;
import org.mycontroller.restclient.philips.hue.model.LightState;
import org.mycontroller.restclient.philips.hue.model.State;

import com.fasterxml.jackson.databind.JavaType;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.0.0
 */

public class DefaultLightsClient extends BaseClient<LightsHandler> implements LightsClient {
    private String authorizedUser;

    public DefaultLightsClient(ClientInfo clientInfo) {
        super(clientInfo, new RestFactory<>(LightsHandler.class));
        this.authorizedUser = (String) clientInfo.getProperties().get(PhilipsHueClient.KEY_AUTHORIZED_USER);
    }

    @Override
    public ClientResponse<Map<String, LightState>> listAll() {
        Response serverResponse = null;
        try {
            serverResponse = restApi().getAll(authorizedUser);
            JavaType javaType = mapResolver().get(Map.class, String.class, LightState.class);
            return new DefaultClientResponse<>(javaType, serverResponse, ResponseCodes.GET_SUCCESS_200);
        } finally {
            if (serverResponse != null) {
                serverResponse.close();
            }
        }
    }

    @Override
    public ClientResponse<Map<String, String>> listNew() {
        Response serverResponse = null;
        try {
            serverResponse = restApi().getNew(authorizedUser);
            JavaType javaType = mapResolver().get(Map.class, String.class, String.class);
            return new DefaultClientResponse<>(javaType, serverResponse, ResponseCodes.GET_SUCCESS_200);
        } finally {
            if (serverResponse != null) {
                serverResponse.close();
            }
        }
    }

    @Override
    public ClientResponse<String> searchNew() {
        Response serverResponse = null;
        try {
            serverResponse = restApi().searchNew(authorizedUser);
            JavaType javaType = simpleResolver().get(String.class);
            return new DefaultClientResponse<>(javaType, serverResponse, ResponseCodes.GET_SUCCESS_200);
        } finally {
            if (serverResponse != null) {
                serverResponse.close();
            }
        }
    }

    @Override
    public ClientResponse<LightState> state(String id) {
        Response serverResponse = null;
        try {
            serverResponse = restApi().get(authorizedUser, id);
            JavaType javaType = simpleResolver().get(LightState.class);
            return new DefaultClientResponse<>(javaType, serverResponse, ResponseCodes.GET_SUCCESS_200);
        } finally {
            if (serverResponse != null) {
                serverResponse.close();
            }
        }
    }

    @Override
    public ClientResponse<String> updateName(String id, String name) {
        Response serverResponse = null;
        try {
            serverResponse = restApi().updateName(authorizedUser, id, name);
            JavaType javaType = simpleResolver().get(String.class);
            return new DefaultClientResponse<>(javaType, serverResponse, ResponseCodes.UPDATE_SUCCESS_200);
        } finally {
            if (serverResponse != null) {
                serverResponse.close();
            }
        }
    }

    @Override
    public ClientResponse<String> updateState(String id, State state) {
        Response serverResponse = null;
        try {
            serverResponse = restApi().updateState(authorizedUser, id, state);
            JavaType javaType = simpleResolver().get(String.class);
            return new DefaultClientResponse<>(javaType, serverResponse, ResponseCodes.UPDATE_SUCCESS_200);
        } finally {
            if (serverResponse != null) {
                serverResponse.close();
            }
        }
    }

    @Override
    public ClientResponse<Empty> delete(String id) {
        Response serverResponse = null;
        try {
            serverResponse = restApi().delete(authorizedUser, id);
            JavaType javaType = simpleResolver().get(Empty.class);
            return new DefaultClientResponse<>(javaType, serverResponse, ResponseCodes.DELETE_SUCCESS_200);
        } finally {
            if (serverResponse != null) {
                serverResponse.close();
            }
        }
    }

}
