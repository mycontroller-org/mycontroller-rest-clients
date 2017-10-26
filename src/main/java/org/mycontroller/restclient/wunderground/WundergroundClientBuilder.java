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

import java.net.URI;
import java.net.URISyntaxException;

import org.mycontroller.restclient.core.BaseClientBuilder;
import org.mycontroller.restclient.core.ClientInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.0.0
 */
@Slf4j
public class WundergroundClientBuilder extends BaseClientBuilder<WundergroundClient> {
    @Override
    public WundergroundClient build() {
        URI uri = null;
        try {
            uri = new URI(WundergroundClient.URI);
        } catch (URISyntaxException ex) {
            _logger.error("Error,", ex);
        }
        ClientInfo clientInfo = new ClientInfo(getUri() != null ? getUri() : uri,
                getTrustHostType(), getUsername(), getPassword(), getHeaders(), getProperties());
        return new WundergroundClientImpl(clientInfo);
    }
}
