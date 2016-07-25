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
package org.mycontroller.standalone.restclient.emoncms;

import java.net.URI;

import org.mycontroller.standalone.restclient.ClientBase;
import org.mycontroller.standalone.restclient.ClientResponse;
import org.mycontroller.standalone.restclient.RestFactory;
import org.mycontroller.standalone.restclient.RestFactory.TRUST_HOST_TYPE;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 0.0.3
 */
public class EmoncmsClientImpl extends ClientBase<EmoncmsRestAPI> implements EmoncmsClient {
    public static final String DEFAULT_EMONCMS_URL = "https://emoncms.org";
    private String writeApiKey;
    private StringBuilder jsonBuilder = new StringBuilder();

    public EmoncmsClientImpl(String url, String writeApiKey, TRUST_HOST_TYPE trustHostType)
            throws Exception {
        super(new URI(url), new RestFactory<EmoncmsRestAPI>(EmoncmsRestAPI.class));
        this.writeApiKey = writeApiKey;
    }

    @Override
    public ClientResponse<String> send(String key, String value) {
        jsonBuilder.setLength(0);
        jsonBuilder.append("{\"").append(key).append("\":\"").append(value).append("\"}");
        return new ClientResponse<String>(restApi().send(writeApiKey, jsonBuilder.toString()), 200);
    }
}
