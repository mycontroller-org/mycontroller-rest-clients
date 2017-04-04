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
package org.mycontroller.restclient.influxdb;

import static com.google.common.base.Preconditions.checkArgument;

import javax.ws.rs.core.Response;

import org.mycontroller.restclient.core.BaseClient;
import org.mycontroller.restclient.core.ClientInfo;
import org.mycontroller.restclient.core.ClientResponse;
import org.mycontroller.restclient.core.DefaultClientResponse;
import org.mycontroller.restclient.core.jaxrs.Empty;
import org.mycontroller.restclient.core.jaxrs.ResponseCodes;
import org.mycontroller.restclient.core.jaxrs.RestFactory;
import org.mycontroller.restclient.influxdb.model.Pong;
import org.mycontroller.restclient.influxdb.model.QueryResult;

import com.fasterxml.jackson.databind.JavaType;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.0.0
 */
@Slf4j
public class InfluxDBClientImpl extends BaseClient<InfluxDBHandler> implements InfluxDBClient {
    private String database;
    private static final String EPOCH = "ms"; //h,m,s,ms,u,ns

    public InfluxDBClientImpl(ClientInfo clientInfo) {
        super(clientInfo, new RestFactory<>(InfluxDBHandler.class));
        checkArgument(clientInfo.getProperties().get(InfluxDBClient.KEY_DATABASE) != null,
                "Database property['" + InfluxDBClient.KEY_DATABASE + "'] is missing");
        database = (String) clientInfo.getProperties().get(InfluxDBClient.KEY_DATABASE);
    }

    @Override
    public Pong ping() {
        Response serverResponse = null;
        try {
            serverResponse = restApi().ping();
            JavaType javaType = simpleResolver().get(Empty.class);
            ClientResponse<Empty> result = new DefaultClientResponse<>(javaType, serverResponse,
                    ResponseCodes.NO_CONTENT_204);
            if (result.isSuccess()) {
                return Pong.builder()
                        .version(serverResponse.getHeaderString("X-Influxdb-Version"))
                        .reachable(true)
                        .build();
            }
            return Pong.builder()
                    .reachable(false)
                    .statusCode(result.getStatusCode())
                    .errorMessage(result.getErrorMsg())
                    .build();
        } finally {
            if (serverResponse != null) {
                serverResponse.close();
            }
        }

    }

    @Override
    public ClientResponse<String> write(String measurement, String tags, Long timestamp, String value) {
        Response serverResponse = null;
        StringBuilder dataBuilder = new StringBuilder();
        try {
            //timestamp format: 1434067467000000000
            dataBuilder.setLength(0);
            dataBuilder.append(measurement);
            if (tags != null && tags.length() > 0) {
                dataBuilder.append(",").append(tags.replaceAll(" ", "_"));
            } else {

            }
            dataBuilder.append(" value=").append(value).append(" ").append(timestamp).append("000000");
            serverResponse = restApi().write(database, dataBuilder.toString());
            JavaType javaType = simpleResolver().get(String.class);
            return new DefaultClientResponse<>(javaType, serverResponse, ResponseCodes.NO_CONTENT_204);
        } finally {
            if (serverResponse != null) {
                serverResponse.close();
            }
        }
    }

    @Override
    public ClientResponse<QueryResult> query(String query, boolean pretty) {
        _logger.debug("Query:{}", query);
        Response serverResponse = null;
        try {
            serverResponse = restApi().query(database, query, pretty, EPOCH);
            JavaType javaType = simpleResolver().get(QueryResult.class);
            return new DefaultClientResponse<>(javaType, serverResponse, ResponseCodes.GET_SUCCESS_200);
        } finally {
            if (serverResponse != null) {
                serverResponse.close();
            }
        }
    }

    @Override
    public ClientResponse<QueryResult> query(String query) {
        return query(query, false);
    }

    private ClientResponse<QueryResult> managementQueryInternal(String query, String database) {
        _logger.debug("Query:[{}], database:{}", query, database);
        Response serverResponse = null;
        try {
            serverResponse = restApi().managementQuery(database, query, false, EPOCH);
            JavaType javaType = simpleResolver().get(QueryResult.class);
            return new DefaultClientResponse<>(javaType, serverResponse, ResponseCodes.GET_SUCCESS_200);
        } finally {
            if (serverResponse != null) {
                serverResponse.close();
            }
        }
    }

    @Override
    public ClientResponse<QueryResult> managementQuery(String query) {
        return managementQueryInternal(query, database);

    }

    @Override
    public ClientResponse<QueryResult> createDatabase(String database) {
        return managementQueryInternal("CREATE DATABASE \"" + database + "\"", null);
    }

    @Override
    public ClientResponse<QueryResult> dropDatabase(String database) {
        return managementQueryInternal("DROP DATABASE \"" + database + "\"", null);
    }

}
