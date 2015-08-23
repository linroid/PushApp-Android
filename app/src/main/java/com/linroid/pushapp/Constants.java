/*
 * Copyright (c) linroid 2015.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.linroid.pushapp;

/**
 * Created by linroid on 7/24/15.
 */
public class Constants {
    public static final String AUTH_HEADER = "X-Token";

    public static final String SP_FILE_NAME = "pushapp";

    public static final String SP_TOKEN = "token";
    public static final String SP_AUTO_INSTALL_CONFIRMED = "auto_install_confirmed";
    public static final String SP_AUTO_INSTALL = "auto_install";
    public static final String SP_AUTO_OPEN = "auto_open";

    public static final String PUSH_TYPE_PACKAGE = "package";
    public static final String PUSH_TYPE_DEVICE = "device";

    public static final String EXTRA_DEVICE = "device";
    public static final String EXTRA_PACKAGE = "package";

    public static final int DB_VERSION = 2;
    public static final String DB_NAME = "pushapp.db";

    public static final String QRCODE_KEY_AUTH = "auth";
    public static final String QRCODE = "qrcode";
    public static final String QRCODE_KEY_DEVICE = "device";
}
