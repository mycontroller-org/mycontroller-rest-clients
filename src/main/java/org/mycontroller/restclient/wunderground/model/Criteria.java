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
package org.mycontroller.restclient.wunderground.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.0.0
 */
@Getter
@ToString
@Builder
public class Criteria {
    private Features features;
    private String languageCode;
    private String location;
    private String geoIP;

    public Features getFeatures() {
        if (features == null) {
            features = Features.getDefault();
        }
        return features;
    }

    public String getLanguageCode() {
        if (languageCode == null) {
            languageCode = "EN";
        }
        return languageCode;
    }

    public String getLocation() {
        if (location == null) {
            location = "autoip";
        }
        return location;
    }
}
