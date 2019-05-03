/*
 * Copyright (C) 2015 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package icy.manipulator.model;

import icy.manipulator.Icy;

/**
 * @version 2015/06/03 16:39:41
 */
@Icy
public class GenericVariableModel<Value1, Value2> {

    public Value1 value1;

    public Box<Value2> value2;

    public Box<Box<Value2>> nestedValue2;
}