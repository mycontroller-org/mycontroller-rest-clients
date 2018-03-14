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
package org.mycontroller.restclient.twilio.model;

import java.util.Date;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 0.0.3
 */
@Getter
@ToString
public class MessageResponse {
    @SerializedName("sid")
    private String sid;

    @SerializedName("date_created")
    private Date dateCreated;

    @SerializedName("date_updated")
    private Date dateUpdated;

    @SerializedName("date_sent")
    private Date dateSent;

    @SerializedName("account_sid")
    private String accountSid;

    @SerializedName("to")
    private String to;

    @SerializedName("from")
    private String from;

    @SerializedName("messaging_service_sid")
    private String messagingServiceSid;

    @SerializedName("body")
    private String body;

    @SerializedName("status")
    private String status;

    @SerializedName("num_segments")
    private String numSegments;

    @SerializedName("num_media")
    private String numMedia;

    @SerializedName("direction")
    private String direction;

    @SerializedName("api_version")
    private String apiVersion;

    @SerializedName("price")
    private String price;

    @SerializedName("price_unit")
    private String priceUnit;

    @SerializedName("error_code")
    private String errorCode;

    @SerializedName("error_message")
    private String errorMessage;

    @SerializedName("uri")
    private String uri;

    @SerializedName("subresource_uris")
    private Map<String, Object> subresourceUris;

}