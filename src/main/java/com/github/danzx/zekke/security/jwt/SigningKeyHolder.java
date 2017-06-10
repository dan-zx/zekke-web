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
package com.github.danzx.zekke.security.jwt;

import com.github.danzx.zekke.security.KeyFileReader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Signing key holder. Pretty obvious isn't it?
 * 
 * @author Daniel Pedraza-Arcega
 */
@Component
public class SigningKeyHolder {

    private final byte[] signingKey;

    public SigningKeyHolder(@Value("${jwt.key_file.path}") String keyFilePath) {
        this.signingKey = KeyFileReader.fromClasspath(keyFilePath).getKey();
    }

    public byte[] getKey() {
        return signingKey;
    }
}
