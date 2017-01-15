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
package org.mycontroller.restclient.philips.hue.clients;

import java.util.Map;

import org.mycontroller.restclient.core.ClientResponse;
import org.mycontroller.restclient.core.jaxrs.Empty;
import org.mycontroller.restclient.philips.hue.model.LightState;
import org.mycontroller.restclient.philips.hue.model.State;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.0.0
 */

public interface LightsClient {

    ClientResponse<Map<String, LightState>> listAll();

    ClientResponse<Map<String, Object>> listNew();

    ClientResponse<String> searchNew();

    ClientResponse<LightState> state(String id);

    ClientResponse<String> updateName(String id, String name);

    ClientResponse<String> updateState(String id, State state);

    ClientResponse<Empty> delete(String id);
}
