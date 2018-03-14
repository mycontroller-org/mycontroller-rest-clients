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
package org.mycontroller.restclient.plivo.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.1.0
 */

@Getter
@ToString
public class Account {
    @SerializedName("account_type")
    private String accountType;

    private String address;

    @SerializedName("api_id")
    private String apiId;

    @SerializedName("auth_id")
    private String authId;

    @SerializedName("auto_recharge")
    private Boolean autoRecharge;

    @SerializedName("billing_mode")
    private String billingMode;

    @SerializedName("cash_credits")
    private String cashCredits;

    private String city;

    private String name;

    @SerializedName("resource_uri")
    private String resourceUri;

    private String state;

    private String timezone;

}
