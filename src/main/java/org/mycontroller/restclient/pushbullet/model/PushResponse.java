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
package org.mycontroller.restclient.pushbullet.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 0.0.3
 */
@Getter
@ToString
public class PushResponse {

    private String active;
    private String body;
    private String created;
    private String direction;
    private String dismissed;
    private String iden;
    private String modified;

    @SerializedName("receiver_email")
    private String receiverEmail;

    @SerializedName("receiver_email_normalized")
    private String receiverEmailNormalized;

    @SerializedName("receiver_iden")
    private String receiverIden;

    @SerializedName("sender_email")
    private String senderEmail;

    @SerializedName("sender_email_normalized")
    private String senderEmailEormalized;

    @SerializedName("sender_iden")
    private String senderIden;

    @SerializedName("sender_name")
    private String senderName;

    private String title;
    private String type;

}