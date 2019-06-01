/*
 * Copyright (C) 2019 icymanipulator Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package icy.manipulator.property.collection.model;

import java.util.Map;

import icy.manipulator.Icy;

@Icy
public abstract class MapModel {

    @Icy.Property
    public abstract Map<String, Integer> values();

    @Icy.Property
    public abstract int age();
}