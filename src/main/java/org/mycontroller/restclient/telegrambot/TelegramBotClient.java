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
package org.mycontroller.restclient.telegrambot;

import java.util.Map;

import org.mycontroller.restclient.core.RestHeader;
import org.mycontroller.restclient.core.RestHttpClient;
import org.mycontroller.restclient.core.RestHttpResponse;
import org.mycontroller.restclient.core.TRUST_HOST_TYPE;
import org.mycontroller.restclient.telegrambot.model.Message;
import org.mycontroller.restclient.telegrambot.model.Response;
import org.mycontroller.restclient.telegrambot.model.User;

import com.fasterxml.jackson.databind.JavaType;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 3.0.0
 */

public class TelegramBotClient extends RestHttpClient {
    public static final String URL = "https://api.telegram.org";

    private String token;

    private String baseUrl;
    private RestHeader header;

    public TelegramBotClient(String token, TRUST_HOST_TYPE trustHostType) {
        super(trustHostType == null ? TRUST_HOST_TYPE.DEFAULT : trustHostType);
        this.token = token;
        initClient();
    }

    private void initClient() {
        baseUrl = String.format("%s/bot%s", URL, token);
        header = RestHeader.getDefault();
        header.addJsonContentType();
    }

    private Object readResponse(String entity, JavaType javaType) {
        Response response = (Response) readValue(entity, simpleResolver().get(Response.class));
        if (response.isOk()) {
            return readValue(response.getResult(), javaType);
        } else {
            throw new RuntimeException(response.toString());
        }
    }

    public User getMe() {
        RestHttpResponse response = doGet(baseUrl + "/getMe", header, STATUS_CODE.OK.getCode());
        return (User) readResponse(response.getEntity(), simpleResolver().get(User.class));
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> sendMessage(Message message) {
        RestHttpResponse response = doPost(
                baseUrl + "/sendMessage", header, toJsonString(message), STATUS_CODE.OK.getCode());
        return (Map<String, Object>) readResponse(
                response.getEntity(),
                mapResolver().get(Map.class, String.class, Object.class));
    }

}
