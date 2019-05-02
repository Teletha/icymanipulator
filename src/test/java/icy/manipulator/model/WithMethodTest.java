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

import org.junit.jupiter.api.Test;

/**
 * @version 2015/06/08 1:05:17
 */
public class WithMethodTest {

    @Test
    public void methodCall() throws Exception {
        WithMethod icy = WithMethod.with().property("TEST").ice();
        assert icy.getPropertyWith("!").equals("TEST!");
    }
}
