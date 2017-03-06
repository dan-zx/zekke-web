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
package com.github.danzx.zekke.util.constraints.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.github.danzx.zekke.util.constraint.FloatRange;

/**
 * Validate that the float value is between min and max included.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class FloatRangeValidator implements ConstraintValidator<FloatRange, Float>{

    private FloatRange meta;

    @Override
    public void initialize(FloatRange constraintAnnotation) {
        meta = constraintAnnotation;
    }


    @Override
    public boolean isValid(Float value, ConstraintValidatorContext context) {
        if (value == null) return true;
        float fValue = value.floatValue();
        return meta.min() <= fValue && fValue <= meta.max();
    }
}
