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
package org.mycontroller.standalone.restclient.influxdb;

import java.net.URI;

import org.mycontroller.standalone.restclient.ClientBase;
import org.mycontroller.standalone.restclient.ClientResponse;
import org.mycontroller.standalone.restclient.RestFactory;
import org.mycontroller.standalone.restclient.RestFactory.TRUST_HOST_TYPE;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 1.0.0
 */
public class InfluxdbClientImpl extends ClientBase<InfluxdbRestAPI> implements InfluxdbClient {
    private String database;
    private StringBuilder dataBuilder = new StringBuilder();

    public InfluxdbClientImpl(String url, String database, TRUST_HOST_TYPE trustHostType)
            throws Exception {
        super(new URI(url), new RestFactory<InfluxdbRestAPI>(InfluxdbRestAPI.class));
        this.database = database;
    }

    public InfluxdbClientImpl(String url, String username, String password, String database,
            TRUST_HOST_TYPE trustHostType) throws Exception {
        super(new URI(url), username, password, new RestFactory<InfluxdbRestAPI>(InfluxdbRestAPI.class));
        this.database = database;
    }

    @Override
    public ClientResponse<String> write(String key, String tags, Long timestamp, String value) {
        //timestamp format: 1434067467000000000
        dataBuilder.setLength(0);
        dataBuilder.append(key);
        if (tags != null && tags.length() > 0) {
            dataBuilder.append(",").append(tags.replaceAll(" ", "_"));
        }
        dataBuilder.append(" value=").append(value).append(" ").append(timestamp).append("000000");
        return new ClientResponse<String>(restApi().write(database, dataBuilder.toString()), 204);
    }
}
