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
package com.github.danzx.zekke.security.jwt.jjwt;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.danzx.zekke.domain.User;
import com.github.danzx.zekke.security.jwt.SigningKeyHolder;

import org.junit.Test;

public class JjwtFactoryTest {

    private static final String JWT_PATTERN = ".+\\..+\\..+";

    @Test
    public void shouldCreateToken() {
        SigningKeyHolder keyHolder = new SigningKeyHolder("keys/test1.key");
        String issuer = "testIssuer";
        long expirationTimeInMinutes = 1L;
        JjwtFactory jwtFactory = new JjwtFactory(expirationTimeInMinutes, issuer, keyHolder);
        String token = jwtFactory.newToken(User.Role.ANONYMOUS);
        assertThat(token).isNotNull().isNotBlank().matches(JWT_PATTERN);
    }
}
