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
package org.mycontroller.restclient.wunderground;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.mycontroller.restclient.core.McHeader;
import org.mycontroller.restclient.core.McHttpClient;
import org.mycontroller.restclient.core.McHttpResponse;
import org.mycontroller.restclient.core.TRUST_HOST_TYPE;
import org.mycontroller.restclient.wunderground.model.Criteria;
import org.mycontroller.restclient.wunderground.model.WUResponse;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.1.0
 */

public class WundergroundClient extends McHttpClient {
    public static final String URL = "https://api.wunderground.com";
    public static final String UPLOAD_URL = "https://weatherstation.wunderground.com"
            + "/weatherstation/updateweatherstation.php";

    private String apiKey;

    private String baseUrl;
    private McHeader header;

    public WundergroundClient(String apiKey, TRUST_HOST_TYPE trustHostType) {
        super(trustHostType == null ? TRUST_HOST_TYPE.DEFAULT : trustHostType);
        this.apiKey = apiKey;
        initClient();
    }

    private void initClient() {
        baseUrl = URL;
        header = McHeader.getDefault();
        header.addJsonContentType();
    }

    public WUResponse query(Criteria criteria) {
        HashMap<String, Object> queryParams = null;
        if (criteria.getGeoIP() != null) {
            queryParams = new HashMap<String, Object>();
            queryParams.put("geo_ip", criteria.getGeoIP());
        }
        // sample url: /api/{key}/{features}/lang:{languageCode}/q/{location}.json
        McHttpResponse response = doGet(baseUrl +
                MessageFormat.format("/api/{0}/{1}/lang:{2}/q/{3}.json",
                        apiKey, criteria.getFeatures().queryString(),
                        criteria.getLanguageCode(), criteria.getLocation()),
                queryParams, header, STATUS_CODE.OK.getCode());
        return (WUResponse) readValue(response.getEntity(), simpleResolver().get(WUResponse.class));
    }

    // refer: http://wiki.wunderground.com/index.php/PWS_-_Upload_Protocol
    // should add: ID, PASSWORD, action, dateutc
    public String send(Map<String, Object> data) {
        updateOnNull(data, "action", "updateraw");
        updateOnNull(data, "dateutc", "now");
        McHttpResponse response = doGet(UPLOAD_URL, data, header, STATUS_CODE.OK.getCode());
        return response.getEntity();
    }
}
