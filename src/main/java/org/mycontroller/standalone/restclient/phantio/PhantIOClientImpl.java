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

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.mycontroller.standalone.restclient.ClientBase;
import org.mycontroller.standalone.restclient.ClientResponse;
import org.mycontroller.standalone.restclient.RestFactory;
import org.mycontroller.standalone.restclient.RestFactory.TRUST_HOST_TYPE;
import org.mycontroller.standalone.restclient.phantio.model.PostResponse;
import org.mycontroller.standalone.restclient.phantio.model.Stats;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 0.0.3
 */
public class PhantIOClientImpl extends ClientBase<PhantIORestAPI> implements PhantIOClient {
    public static final String DEFAULT_PHANTIO_URL = "https://data.sparkfun.com";
    public String publicKey = null;
    public String privateKey = null;
    private JsonNodeFactory jsonFactory = new JsonNodeFactory(false);

    public PhantIOClientImpl(String url, String publicKey, String privateKey, TRUST_HOST_TYPE trustHostType)
            throws Exception {
        super(new URI(url), new RestFactory<PhantIORestAPI>(PhantIORestAPI.class));
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        updateHeader("Phant-Private-Key", privateKey);
    }

    @Override
    public ClientResponse<String> clear() {
        return new ClientResponse<String>(String.class, restApi().clear(publicKey), 202);
    }

    @Override
    public ClientResponse<PostResponse> post(String key, String value) {
        ObjectNode objNode = jsonFactory.objectNode();
        objNode.put(key, value);
        return new ClientResponse<PostResponse>(PostResponse.class, restApi().post(publicKey, objNode), 200);
    }

    @Override
    public ClientResponse<Stats> stats() {
        return new ClientResponse<Stats>(Stats.class, restApi().stats(publicKey), 200);
    }

    @Override
    public ClientResponse<List<HashMap<String, String>>> get(String timezone, Long limit) {
        return new ClientResponse<List<HashMap<String, String>>>(HashMap.class,
                restApi().get(publicKey, timezone, limit), 200, true);
    }

    @Override
    public ClientResponse<List<HashMap<String, String>>> get(Long limit) {
        return this.get(TimeZone.getDefault().getID(), limit);
    }
}
