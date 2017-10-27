/*
 * Copyright 2015-2017 Jeeva Kandasamy (jkandasa@gmail.com)
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

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;

import javax.ws.rs.core.Response;

import org.mycontroller.restclient.core.BaseClient;
import org.mycontroller.restclient.core.ClientInfo;
import org.mycontroller.restclient.core.ClientResponse;
import org.mycontroller.restclient.core.DefaultClientResponse;
import org.mycontroller.restclient.core.jaxrs.ResponseCodes;
import org.mycontroller.restclient.core.jaxrs.RestFactory;
import org.mycontroller.restclient.wunderground.model.Criteria;
import org.mycontroller.restclient.wunderground.model.WUResponse;

import com.fasterxml.jackson.databind.JavaType;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.0.0
 */
@Slf4j
public class WundergroundClientImpl extends BaseClient<WundergroundHandler> implements WundergroundClient {
    private String apiKey;

    public WundergroundClientImpl(ClientInfo clientInfo) {
        super(clientInfo, new RestFactory<>(WundergroundHandler.class));
        checkArgument(clientInfo.getProperties().get(WundergroundClient.KEY_API_KEY) != null,
                "'" + WundergroundClient.KEY_API_KEY + "'] is missing");
        apiKey = (String) clientInfo.getProperties().get(WundergroundClient.KEY_API_KEY);
    }

    @Override
    public ClientResponse<WUResponse> query(Criteria criteria) {
        Response serverResponse = null;
        try {
            _logger.debug("{}", criteria);
            StringBuilder builder = new StringBuilder();
            // sample url: /api/{key}/{features}/lang:{languageCode}/q/{location}.json
            builder.append("/api/").append(apiKey)
                    .append("/").append(criteria.getFeatures().queryString())
                    .append("/lang:").append(criteria.getLanguageCode())
                    .append("/q/").append(criteria.getLocation()).append(".json");

            HashMap<String, Object> queryParams = new HashMap<String, Object>();
            if (criteria.getGeoIP() != null) {
                queryParams.put("geo_ip", criteria.getGeoIP());
            }

            serverResponse = get(builder.toString(), queryParams);
            //serverResponse = restApi().query(apiKey, criteria.getFeatures().queryString(), criteria.getLanguageCode(),
            //        criteria.getLocation(), criteria.getGeoIP());
            JavaType javaType = simpleResolver().get(WUResponse.class);
            ClientResponse<WUResponse> result = new DefaultClientResponse<>(javaType, serverResponse,
                    ResponseCodes.GET_SUCCESS_200);
            return result;
        } finally {
            if (serverResponse != null) {
                serverResponse.close();
            }
        }

    }
}
