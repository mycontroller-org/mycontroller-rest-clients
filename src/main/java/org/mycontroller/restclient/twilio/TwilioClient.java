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
package org.mycontroller.restclient.twilio;

import java.text.MessageFormat;

import org.mycontroller.restclient.core.McHeader;
import org.mycontroller.restclient.core.McHttpClient;
import org.mycontroller.restclient.core.McHttpResponse;
import org.mycontroller.restclient.core.TRUST_HOST_TYPE;
import org.mycontroller.restclient.twilio.model.Message;
import org.mycontroller.restclient.twilio.model.MessageResponse;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.1.0
 */

public class TwilioClient extends McHttpClient {
    public static final String URL = "https://api.twilio.com";
    public static final String VERSION = "2010-04-01";

    private String accountSid;
    private String authToken;

    private String baseUrl;
    private McHeader header;

    public TwilioClient(String accountSid, String authToken, TRUST_HOST_TYPE trustHostType) {
        super(trustHostType == null ? TRUST_HOST_TYPE.DEFAULT : trustHostType);
        this.accountSid = accountSid;
        this.authToken = authToken;
        initClient();
    }

    private void initClient() {
        baseUrl = MessageFormat.format("{0}/{1}/{2}", URL, VERSION, accountSid);
        header = McHeader.getDefault();
        header.addJsonContentType();
        header.addAuthorization(accountSid, authToken);
    }

    public MessageResponse sendMessage(Message message) {
        McHttpResponse response = doPost(baseUrl + "/Messages", header, toJsonString(message),
                STATUS_CODE.CREATED.getCode());
        return gson.fromJson(response.getEntity(), MessageResponse.class);
    }

}
