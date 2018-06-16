/*
 * Copyright 2017 Daniel Pedraza-Arcega
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.danzx.zekke.config;

/**
 * Available profiles in the application.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class Profiles {

    public static final String DEVELOPMENT = "dev";
    public static final String STAGING = "stage";

    protected Profiles() { 
        throw new AssertionError();
    }
}
