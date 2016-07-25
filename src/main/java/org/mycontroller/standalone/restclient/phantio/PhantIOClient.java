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
package org.mycontroller.standalone.restclient.phantio;

import java.util.HashMap;
import java.util.List;

import org.mycontroller.standalone.restclient.ClientResponse;
import org.mycontroller.standalone.restclient.IRestClient;
import org.mycontroller.standalone.restclient.phantio.model.PostResponse;
import org.mycontroller.standalone.restclient.phantio.model.Stats;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 1.0.0
 */
public interface PhantIOClient extends IRestClient {
    ClientResponse<String> clear();

    ClientResponse<PostResponse> post(String key, String value);

    ClientResponse<List<HashMap<String, String>>> get(String timezone, Long limit);

    ClientResponse<List<HashMap<String, String>>> get(Long limit);

    ClientResponse<Stats> stats();

}
