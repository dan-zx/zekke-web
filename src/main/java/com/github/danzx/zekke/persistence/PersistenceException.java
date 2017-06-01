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
package com.github.danzx.zekke.persistence;

import com.github.danzx.zekke.exception.AppException;

/**
 * Exception thrown at data access/persistance.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class PersistenceException extends AppException {

    private static final long serialVersionUID = 5789086275216533762L;

    private PersistenceException(BaseAppExceptionBuilder<? extends AppException> builder) {
        super(builder);
    }

    public static class Builder extends BaseAppExceptionBuilder<PersistenceException> {

        @Override
        public PersistenceException build() {
            return new PersistenceException(this);
        }
    }
}
