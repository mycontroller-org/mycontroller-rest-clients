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
package org.mycontroller.restclient.pushbullet;

import java.text.MessageFormat;
import java.util.List;

import org.mycontroller.restclient.core.McHeader;
import org.mycontroller.restclient.core.McHttpClient;
import org.mycontroller.restclient.core.McHttpResponse;
import org.mycontroller.restclient.core.TRUST_HOST_TYPE;
import org.mycontroller.restclient.pushbullet.model.Device;
import org.mycontroller.restclient.pushbullet.model.Devices;
import org.mycontroller.restclient.pushbullet.model.Push;
import org.mycontroller.restclient.pushbullet.model.PushResponse;
import org.mycontroller.restclient.pushbullet.model.User;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.1.0
 */

public class PushbulletClient extends McHttpClient {
    public static final String URL = "https://api.pushbullet.com";
    public static final String VERSION = "v2";

    private String authToken;

    private String baseUrl;
    private McHeader header;

    public PushbulletClient(String authToken, TRUST_HOST_TYPE trustHostType) {
        super(trustHostType == null ? TRUST_HOST_TYPE.DEFAULT : trustHostType);
        this.authToken = authToken;
        initClient();
    }

    public PushResponse createPush(Push push) {
        McHttpResponse response = doPost(baseUrl + "/pushes", header, toJsonString(push), STATUS_CODE.OK.getCode());
        return gson.fromJson(response.getEntity(), PushResponse.class);
    }

    public User currentUser() {
        McHttpResponse response = doGet(baseUrl + "/users/me", header, STATUS_CODE.OK.getCode());
        return gson.fromJson(response.getEntity(), User.class);
    }

    public List<Device> devices() {
        McHttpResponse response = doGet(baseUrl + "/devices", header, STATUS_CODE.OK.getCode());
        return gson.fromJson(response.getEntity(), Devices.class).getDevices();
    }

    private void initClient() {
        baseUrl = MessageFormat.format("{0}/{1}", URL, VERSION);
        header = McHeader.getDefault();
        header.addJsonContentType();
        header.put("Access-Token", authToken);
    }

}
