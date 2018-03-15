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
package org.mycontroller.restclient.phantio;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.mycontroller.restclient.core.McHeader;
import org.mycontroller.restclient.core.McHttpClient;
import org.mycontroller.restclient.core.McHttpResponse;
import org.mycontroller.restclient.core.TRUST_HOST_TYPE;
import org.mycontroller.restclient.phantio.model.PostResponse;
import org.mycontroller.restclient.phantio.model.Stats;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.1.0
 */

public class PhantIOClient extends McHttpClient {
    public static final String DEFAULT_URL = "https://data.sparkfun.com";

    private String publicKey;
    private String privateKey;

    private String baseUrl;
    private McHeader header;

    public PhantIOClient(String publicKey, String privateKey, TRUST_HOST_TYPE trustHostType) {
        this(DEFAULT_URL, publicKey, privateKey, trustHostType);
    }

    public PhantIOClient(String baseUrl, String publicKey, String privateKey, TRUST_HOST_TYPE trustHostType) {
        super(trustHostType == null ? TRUST_HOST_TYPE.DEFAULT : trustHostType);
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        if (baseUrl.endsWith("/")) {
            this.baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        } else {
            this.baseUrl = baseUrl;
        }
        initClient();
    }

    private void initClient() {
        header = McHeader.getDefault();
        header.addJsonContentType();
        header.put("Phant-Private-Key", privateKey);
    }

    public String clear() {
        McHttpResponse response = doDelete(baseUrl + MessageFormat.format("/input/{0}", publicKey),
                header, STATUS_CODE.ACCEPTED.getCode());
        return response.getEntity();
    }

    public List<Map<String, Object>> get(Long limit) {
        return get(TimeZone.getDefault().getID(), limit);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> get(String timezone, Long limit) {
        HashMap<String, Object> queryParms = new HashMap<String, Object>();
        queryParms.put("timezone", timezone);
        queryParms.put("limit", limit);
        McHttpResponse response = doGet(baseUrl + MessageFormat.format("output/{0}.json", publicKey),
                queryParms, header, STATUS_CODE.OK.getCode());
        return (List<Map<String, Object>>) readValue(response.getEntity(),
                collectionResolver().get(List.class, Map.class, String.class, Object.class));
    }

    public PostResponse post(Map<String, Object> data) {
        McHttpResponse response = doPost(baseUrl + MessageFormat.format("/input/{0}", publicKey),
                header, toJsonString(data), STATUS_CODE.OK.getCode());
        return (PostResponse) readValue(response.getEntity(), simpleResolver().get(PostResponse.class));
    }

    public Stats stats() {
        McHttpResponse response = doGet(baseUrl + MessageFormat.format("/output/{0}/stats.json", publicKey),
                header, STATUS_CODE.OK.getCode());
        return (Stats) readValue(response.getEntity(), simpleResolver().get(Stats.class));
    }

}
