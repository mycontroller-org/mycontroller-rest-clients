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
public class Location {
    private String city;
    private String country;
    private String country_iso3166;
    private String country_name;
    private String distance_km;
    private String distance_mi;
    private String elevation;
    private String id;
    private String l;
    private Double lat;
    private Double latitude;
    private Double lon;
    private Double longitude;
    private String magic;
    private NearbyWeatherStations nearby_weather_stations;
    private String neighborhood;
    private String requesturl;
    private String state;
    private String state_name;
    private String type;
    private String tz_long;
    private String tz_short;
    private String wmo;
    private String wuiurl;
    private String zip;
    private String full;
}
