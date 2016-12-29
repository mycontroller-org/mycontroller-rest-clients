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
package org.mycontroller.restclient.philips.hue.fasterxml.jackson;

import java.io.IOException;
import java.util.HashMap;

import org.mycontroller.restclient.core.jaxrs.fasterxml.jackson.ClientObjectMapper;
import org.mycontroller.restclient.philips.hue.model.State;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.0.0
 */

public class StateSerializer extends JsonSerializer<State> {
    @Override
    public void serialize(State state, JsonGenerator jgen, SerializerProvider sp) throws IOException {
        if (state != null) {
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("on", state.getOn());
            data.put("bri", state.getBri());
            data.put("hue", state.getHue());
            data.put("xy", state.getXy());
            data.put("ct", state.getCt());
            ObjectMapper objectMapper = new ClientObjectMapper();
            objectMapper.writeValueAsString(data);
        } else {
            jgen.writeNull();
        }

    }

}
