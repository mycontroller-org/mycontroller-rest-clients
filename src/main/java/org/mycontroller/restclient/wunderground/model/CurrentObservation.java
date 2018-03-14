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
package org.mycontroller.restclient.wunderground.model;

import java.util.Map;

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
public class CurrentObservation {
    private Double dewpoint_c;
    private Double dewpoint_f;
    private String dewpoint_string;
    private Map<String, Object> estimated;
    private Location display_location;
    private String feelslike_c;
    private String feelslike_f;
    private String feelslike_string;
    private String forecast_url;
    private String heat_index_c;
    private String heat_index_f;
    private String heat_index_string;
    private String history_url;
    private String icon;
    private String icon_url;
    private Image image;
    private Long local_epoch;
    private String local_time_rfc822;
    private String local_tz_long;
    private String local_tz_offset;
    private String local_tz_short;
    private String nowcast;
    private String ob_url;
    private Long observation_epoch;
    private Location observation_location;
    private String observation_time;
    private String observation_time_rfc822;
    private String precip_1hr_in;
    private String precip_1hr_metric;
    private String precip_1hr_string;
    private String precip_today_in;
    private String precip_today_metric;
    private String precip_today_string;
    private String pressure_in;
    private String pressure_mb;
    private String pressure_trend;
    private String relative_humidity;
    private String solarradiation;
    private String station_id;
    private Double temp_c;
    private Double temp_f;
    private String temperature_string;
    private String UV;
    private String visibility_km;
    private String visibility_mi;
    private String weather;
    private Integer wind_degrees;
    private String wind_dir;
    private String wind_gust_kph;
    private String wind_gust_mph;
    private Float wind_kph;
    private Float wind_mph;
    private String wind_string;
    private String windchill_c;
    private String windchill_f;
    private String windchill_string;
}
