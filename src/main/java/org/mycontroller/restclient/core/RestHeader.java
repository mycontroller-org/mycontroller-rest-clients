/*
 * Copyright 2015-2018 Jeeva Kandasamy (jkandasa@gmail.com)
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
package org.mycontroller.restclient.core;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpMessage;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.1.0
 */

public class RestHeader {
    public static RestHeader getDefault() {
        RestHeader header = new RestHeader();
        header.put("User-Agent", "Mozilla/5.0");
        return header;
    }

    private Map<String, Object> map = new HashMap<String, Object>();

    public void addAuthorization(String username, String password) {
        if (username != null) {
            map.put("Authorization",
                    "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes()));
        }
    }

    public void addJsonContentType() {
        map.put("Content-Type", "application/json");
    }

    public Object get(String key) {
        return map.get(key);
    }

    public Set<String> keySets() {
        return map.keySet();
    }

    public void put(String key, String value) {
        map.put(key, value);
    }

    public Object remove(String key) {
        return map.remove(key);
    }

    @Override
    public String toString() {
        return map.toString();
    }

    public void updateHeaders(HttpMessage httpMessage) {
        for (String key : map.keySet()) {
            httpMessage.setHeader(key, String.valueOf(map.get(key)));
        }
    }
}
