/*
 * Copyright (C) 2019 icymanipulator Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package icy.manipulator.property.extend.model;

import icy.manipulator.Icy;
import icy.manipulator.property.model.Mixed;

@Icy
public abstract class MixedRequiredModel extends Mixed {

    @Icy.Property
    public abstract String zip();
}