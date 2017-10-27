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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.0.0
 */
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Features {
    private Integer alerts;
    private Integer almanac;
    private Integer astronomy;
    private Integer conditions;
    private Integer currenthurricane;
    private Integer forecast;
    private Integer forecast10day;
    private Integer geolookup;
    private Integer history;
    private Integer hourly;
    private Integer hourly10day;
    private Integer planner;
    private Integer rawtide;
    private Integer tide;
    private Integer webcams;
    private Integer yesterday;

    public String queryString() {
        StringBuilder builder = new StringBuilder();
        addToBuilder(builder, "alerts", alerts);
        addToBuilder(builder, "almanac", almanac);
        addToBuilder(builder, "astronomy", astronomy);
        addToBuilder(builder, "conditions", conditions);
        addToBuilder(builder, "currenthurricane", currenthurricane);
        addToBuilder(builder, "forecast", forecast);
        addToBuilder(builder, "forecast10day", forecast10day);
        addToBuilder(builder, "geolookup", geolookup);
        addToBuilder(builder, "history", history);
        addToBuilder(builder, "hourly", hourly);
        addToBuilder(builder, "hourly10day", hourly10day);
        addToBuilder(builder, "planner", planner);
        addToBuilder(builder, "rawtide", rawtide);
        addToBuilder(builder, "tide", tide);
        addToBuilder(builder, "webcams", webcams);
        addToBuilder(builder, "yesterday", yesterday);
        return builder.toString();
    }

    private void addToBuilder(StringBuilder builder, String feature, Integer value) {
        if (value != null && value == 1) {
            if (builder.length() > 0) {
                builder.append("/");
            }
            builder.append(feature);
        }
    }

    public static Features getDefault() {
        return Features.builder()
                .conditions(1)
                .geolookup(1)
                .build();
    }
}
