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
package org.mycontroller.restclient.philips.hue;

import org.mycontroller.restclient.core.BaseClientBuilder;
import org.mycontroller.restclient.core.ClientInfo;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.0.0
 */

public class PhilipsHueClientBuilder extends BaseClientBuilder<PhilipsHueClient> {
    public PhilipsHueClient build() {
        ClientInfo clientInfo = new ClientInfo(getUri(), getTrustHostType(), getUsername(), getPassword(),
                getHeaders(), getProperties());
        return new PhilipsHueClientImpl(clientInfo);
    }
}
