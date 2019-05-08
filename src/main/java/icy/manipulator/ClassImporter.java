/*
 * Copyright (C) 2015 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package icy.manipulator;

import java.util.Set;
import java.util.TreeSet;

/**
 * @version 2015/06/06 22:47:16
 */
class ClassImporter {

    /** The base package name. */
    final String basePackage;

    /** The base class name. */
    final String baseClass;

    /** The imported classes. */
    final Set<String> imports = new TreeSet();

    /**
     * @param basePackage
     */
    ClassImporter(String baseClass) {
        int index = baseClass.lastIndexOf(".");

        if (index == -1) {
            this.basePackage = "";
            this.baseClass = baseClass;
        } else {
            this.basePackage = baseClass.substring(0, index);
            this.baseClass = baseClass.substring(index + 1);
        }
    }

    /**
     * <p>
     * Import class.
     * </p>
     */
    String use(Class imported) {
        return use(new Type(imported));
    }

    /**
     * <p>
     * Import class.
     * </p>
     */
    String use(Type imported) {
        if (!imported.isDefault() && !imported.isPrimitive() && !imported.generic) {
            if (!imported.packageName.equals(basePackage) && !imported.toString().startsWith(basePackage + "." + baseClass + ".")) {
                imports.add(imported.toString());
            }
        }
        return imported.className.concat(imported.variable.toString());
    }
}