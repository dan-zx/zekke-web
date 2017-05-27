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
package com.github.danzx.zekke.message;

import java.util.Locale;
import java.util.Optional;

/**
 * This class uses a ThreadLocal to hold a Locale that will be used to interpolate messages to the 
 * correct language.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class LocaleHolder {

    public static final ThreadLocal<Locale> THREAD_LOCAL = new ThreadLocal<Locale>();

    public static Locale get() {
        return Optional.ofNullable(THREAD_LOCAL.get()).orElse(Locale.ROOT);
    }

    public static void set(Locale locale) {
        THREAD_LOCAL.set(locale);
    }

    public static void unset() {
        THREAD_LOCAL.remove();
    }
}
