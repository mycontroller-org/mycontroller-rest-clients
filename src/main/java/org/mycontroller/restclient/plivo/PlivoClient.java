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
package org.mycontroller.restclient.plivo;

import java.text.MessageFormat;
import java.util.Map;

import org.mycontroller.restclient.core.RestHeader;
import org.mycontroller.restclient.core.RestHttpClient;
import org.mycontroller.restclient.core.RestHttpResponse;
import org.mycontroller.restclient.core.TRUST_HOST_TYPE;
import org.mycontroller.restclient.plivo.model.Account;
import org.mycontroller.restclient.plivo.model.Message;
import org.mycontroller.restclient.plivo.model.MessageResponse;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.1.0
 */

public class PlivoClient extends RestHttpClient {
    public static final String URL = "https://api.plivo.com";
    public static final String VERSION = "v1";

    private String authId;
    private String authToken;

    private String baseUrl;
    private RestHeader header;

    public PlivoClient(String authId, String authToken, TRUST_HOST_TYPE trustHostType) {
        super(trustHostType == null ? TRUST_HOST_TYPE.DEFAULT : trustHostType);
        this.authId = authId;
        this.authToken = authToken;
        initClient();
    }

    private void initClient() {
        baseUrl = String.format("%s/%s/Account/%s", URL, VERSION, authId);
        header = RestHeader.getDefault();
        header.addJsonContentType();
        header.addAuthorization(authId, authToken);
    }

    public Account accountDetails() {
        RestHttpResponse response = doGet(baseUrl + "/", header, STATUS_CODE.OK.getCode());
        return (Account) readValue(response.getEntity(), simpleResolver().get(Account.class));
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> messageStatus(String messageUuid) {
        RestHttpResponse response = doGet(baseUrl + MessageFormat.format("/Message/{0}/", messageUuid), header,
                STATUS_CODE.OK.getCode());
        return (Map<String, Object>) readValue(response.getEntity(),
                mapResolver().get(Map.class, String.class, Object.class));
    }

    public MessageResponse sendMessage(Message message) {
        RestHttpResponse response = doPost(baseUrl + "/Message/", header, toJsonString(message),
                STATUS_CODE.ACCEPTED.getCode());
        return (MessageResponse) readValue(response.getEntity(), simpleResolver().get(MessageResponse.class));
    }
}
