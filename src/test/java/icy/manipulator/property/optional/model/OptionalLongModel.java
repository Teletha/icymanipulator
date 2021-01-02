/*
 * Copyright (C) 2021 icymanipulator Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package icy.manipulator.property.optional.model;

import java.util.OptionalLong;

import icy.manipulator.Icy;

@Icy
public interface OptionalLongModel {

    @Icy.Property
    OptionalLong value();
}