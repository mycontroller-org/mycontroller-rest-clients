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
package org.mycontroller.restclient.emoncms;

import java.util.HashMap;
import java.util.Map;

import org.mycontroller.restclient.core.McHeader;
import org.mycontroller.restclient.core.McHttpClient;
import org.mycontroller.restclient.core.McHttpResponse;
import org.mycontroller.restclient.core.TRUST_HOST_TYPE;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.1.0
 */

public class EmoncmsClient extends McHttpClient {
    public static final String DEFAULT_URL = "https://emoncms.org";

    private String writeApiKey;

    private String baseUrl;
    private McHeader header;

    public EmoncmsClient(String url, String writeApiKey, TRUST_HOST_TYPE trustHostType) {
        super(trustHostType == null ? TRUST_HOST_TYPE.DEFAULT : trustHostType);
        baseUrl = url;
        this.writeApiKey = writeApiKey;
        initClient();
    }

    public EmoncmsClient(String writeApiKey, TRUST_HOST_TYPE trustHostType) {
        this(DEFAULT_URL, writeApiKey, trustHostType);
    }

    private void initClient() {
        header = McHeader.getDefault();
        header.addJsonContentType();
        header.put("Authorization", "Bearer " + writeApiKey);
    }

    public String post(Map<String, Object> data) {
        return post("0", data, null);
    }

    public String post(String node, Map<String, Object> data) {
        return post(node, data, null);
    }

    public String post(String node, Map<String, Object> data, Long timestampUnix) {
        Map<String, Object> queryParameters = new HashMap<String, Object>();
        queryParameters.put("fulljson", toJsonString(data));
        queryParameters.put("node", node);
        if (timestampUnix != null) {
            queryParameters.put("time", timestampUnix);
        }
        McHttpResponse response = doGet(baseUrl + "/input/post", queryParameters, header, STATUS_CODE.OK.getCode());
        return response.getEntity();
    }

}
