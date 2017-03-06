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
package com.github.danzx.zekke.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.danzx.zekke.test.paramprovider.BlankStringProvider;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class StringsTest {

    @Test
    public void shouldReturnFalseWithNonBlankStrings() {
        assertThat(Strings.isBlank("\nnon blank ")).isFalse();
    }

    @Test
    @Parameters(source = BlankStringProvider.class)
    public void shouldReturnTrueWithBlankStrings(String blankString) {
        assertThat(Strings.isBlank(blankString)).isTrue();
    }
}