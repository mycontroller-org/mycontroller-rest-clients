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
package org.mycontroller.restclient.philipshue;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.mycontroller.restclient.core.McHeader;
import org.mycontroller.restclient.core.McHttpClient;
import org.mycontroller.restclient.core.McHttpResponse;
import org.mycontroller.restclient.core.TRUST_HOST_TYPE;
import org.mycontroller.restclient.philipshue.model.LightState;
import org.mycontroller.restclient.philipshue.model.State;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.1.0
 */

public class LightsClient extends McHttpClient {
    private String username;

    private String baseUrl;
    private McHeader header;

    public LightsClient(String baseUrl, String username, TRUST_HOST_TYPE trustHostType) {
        super(trustHostType == null ? TRUST_HOST_TYPE.DEFAULT : trustHostType);
        this.username = username;
        if (baseUrl.endsWith("/")) {
            this.baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        } else {
            this.baseUrl = baseUrl;
        }
        initClient();
    }

    private void initClient() {
        baseUrl = MessageFormat.format("{0}/api/{1}", baseUrl, username);
        header = McHeader.getDefault();
        header.addJsonContentType();
    }

    public void delete(String id) {
        doDelete(baseUrl + MessageFormat.format("/lights/{0}", id), header, STATUS_CODE.OK.getCode());
    }

    @SuppressWarnings("unchecked")
    public Map<String, LightState> listAll() {
        McHttpResponse response = doGet(baseUrl + "/lights", header, STATUS_CODE.OK.getCode());
        return (Map<String, LightState>) readValue(response.getEntity(),
                mapResolver().get(Map.class, String.class, LightState.class));
    }

    @SuppressWarnings("unchecked")
    public Map<String, LightState> listNew() {
        McHttpResponse response = doGet(baseUrl + "/lights/new", header, STATUS_CODE.OK.getCode());
        return (Map<String, LightState>) readValue(response.getEntity(),
                mapResolver().get(Map.class, String.class, LightState.class));
    }

    public String searchNew() {
        McHttpResponse response = doPost(baseUrl + "/lights", header, null, STATUS_CODE.OK.getCode());
        return (String) readValue(response.getEntity(), simpleResolver().get(String.class));
    }

    public LightState state(String id) {
        McHttpResponse response = doGet(baseUrl + MessageFormat.format("/lights/{0}", id),
                header, STATUS_CODE.OK.getCode());
        return (LightState) readValue(response.getEntity(), simpleResolver().get(LightState.class));
    }

    public void updateName(String id, String name) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("name", name);
        doPut(baseUrl + MessageFormat.format("/lights/{0}", id), header, toJsonString(data), STATUS_CODE.OK.getCode());
    }

    public void updateState(String id, State state) {
        doPut(baseUrl + MessageFormat.format("/lights/{0}/state", id), header, toJsonString(state),
                STATUS_CODE.OK.getCode());
    }
}
