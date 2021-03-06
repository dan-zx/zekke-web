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
package com.github.danzx.zekke.persistence.internal;

/**
 * Manages and retrieves sequence values.
 * 
 * @author Daniel Pedraza-Arcega
 */
public interface SequenceManager {

    /**
     * Gets the current value of the given sequence.
     * 
     * @param sequence a sequence.
     * @return the current value.
     */
    long getCurrentSequenceValue(Sequence sequence);

    /**
     * Gets the next value of the given sequence.
     * 
     * @param sequence a sequence.
     * @return the next value value.
     */
    long getNextSequenceValue(Sequence sequence);

    /**
     * Sets a new value to the given sequence
     * 
     * @param sequence a sequence.
     * @param newValue the new value.
     */
    void setSequenceValue(Sequence sequence, long newValue);
}
