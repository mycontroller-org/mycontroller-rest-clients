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

import org.mycontroller.restclient.core.ClientResponse;
import org.mycontroller.restclient.influxdb.model.Pong;
import org.mycontroller.restclient.influxdb.model.QueryResult;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.0.0
 */

public interface InfluxDBClient {
    String KEY_DATABASE = "db";

    Pong ping();

    ClientResponse<String> write(String measurement, String tags, Long timestamp, String data);

    ClientResponse<QueryResult> managementQuery(String query);

    ClientResponse<QueryResult> query(String query);

    ClientResponse<QueryResult> query(String query, boolean pretty);

    ClientResponse<QueryResult> createDatabase(String database);

    ClientResponse<QueryResult> dropDatabase(String database);
}
