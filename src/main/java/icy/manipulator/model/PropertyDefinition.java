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

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

import icy.manipulator.CodeAnalyzer;
import icy.manipulator.Type;

public class PropertyDefinition {

    /** The actual source element. */
    public final ExecutableElement element;

    /** The property name. */
    public final String name;

    /** The type name. */
    public final Type type;

    /** The property state. */
    public final boolean isArbitrary;

    /** The state. */
    public String derive;

    /** The state. */
    public boolean isDerived;

    /** The next property. */
    public String next;

    public PropertyDefinition nextProperty;

    /**
     * @param method
     */
    public PropertyDefinition(ExecutableElement method) {
        this.element = method;
        this.name = method.getSimpleName().toString();
        this.type = Type.of(method.getReturnType());
        this.isArbitrary = !method.getModifiers().contains(Modifier.ABSTRACT);
    }

    /**
     * Compute visibilitt of setter method.
     * 
     * @return A visibility.
     */
    public String setterVisibility() {
        return isDerived ? "protected" : "public";
    }

    /**
     * Compute assignable interface name of this property.
     * 
     * @return An interface name.
     */
    public String assignableInterfaceName() {
        if (isArbitrary) {
            return CodeAnalyzer.ArbitraryInterface;
        } else {
            return CodeAnalyzer.Assignable + Character.toUpperCase(name.charAt(0)) + name.substring(1);
        }
    }

    /**
     * Compute assignable interface name of this property.
     * 
     * @return An interface name.
     */
    public String assignableInterfaceType(String last) {
        String type = assignableInterfaceName();

        if (nextProperty != null) {
            type = type + "<" + nextProperty.assignableInterfaceType(last) + ">";
        } else {
            type = type + "<" + last + ">";
        }
        return type;
    }

    /**
     * Compute capitalized property name.
     * 
     * @return
     */
    public String capitalizeName() {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    /**
     * @param className
     * @return
     */
    public String nextAssignable(String className) {
        if (nextProperty == null) {
            return className;
        } else {
            return nextProperty.assignableInterfaceType(className);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name).append("(").append(type).append(")");
        return builder.toString();
    }
}