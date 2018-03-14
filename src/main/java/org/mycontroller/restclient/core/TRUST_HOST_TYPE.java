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

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 2.0.0
 */

public enum TRUST_HOST_TYPE {

    DEFAULT("Default"),
    ANY("Any");
    public static TRUST_HOST_TYPE fromString(String text) {
        if (text != null) {
            for (TRUST_HOST_TYPE type : TRUST_HOST_TYPE.values()) {
                if (text.equalsIgnoreCase(type.getText())) {
                    return type;
                }
            }
        }
        return null;
    }

    public static TRUST_HOST_TYPE get(int id) {
        for (TRUST_HOST_TYPE type : values()) {
            if (type.ordinal() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException(String.valueOf(id));
    }

    private String value;

    private TRUST_HOST_TYPE(String value) {
        this.value = value;
    }

    public String getText() {
        return this.value;
    }

}
