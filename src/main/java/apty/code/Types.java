/*
 * Copyright (C) 2019 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package apty.code;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import icy.manipulator.util.Lists;

public class Types implements Codable {

    private final List<Type> types;

    /**
     * @param types
     */
    public Types(Type... types) {
        this(List.of(types));
    }

    /**
     * @param types
     */
    public Types(List<Type> types) {
        this.types = types;
    }

    /**
     * List up all types.
     * 
     * @return
     */
    public Stream<Type> stream() {
        return types.stream();
    }

    /**
     * Create new {@link Types} which all variables are raw.
     * 
     * @return A new {@link Types}.
     */
    public Types raw() {
        return new Types(types.stream().map(Type::raw).collect(Collectors.toUnmodifiableList()));
    }

    /**
     * Get type variable at head.
     * 
     * @return
     */
    public Type head() {
        return types.get(0);
    }

    /**
     * Create merged variable types at head.
     * 
     * @return
     */
    public Types head(Types types) {
        return new Types(Lists.merge(types.types, this.types));
    }

    /**
     * Create merged variable types at tail.
     * 
     * @return
     */
    public Types tail(Types types) {
        return new Types(Lists.merge(this.types, types.types));
    }

    /**
     * Create new {@link Types} which the specified variable is prepended at head.
     * 
     * @param variable A variable to prepend.
     * @return A new {@link Types}.
     */
    public Types prepend(String variable) {
        return prepend(Type.var(variable));
    }

    /**
     * Create new {@link Types} which the specified variable is prepended at head.
     * 
     * @param variable A variable to prepend.
     * @return A new {@link Types}.
     */
    public Types prepend(Type type) {
        List<Type> list = new ArrayList();
        list.add(type);
        list.addAll(types);

        return new Types(list);
    }

    /**
     * Create new {@link Types} which the specified variable is appended at tail.
     * 
     * @param variable A variable to append.
     * @return A new {@link Types}.
     */
    public Types append(String variable) {
        List<Type> list = new ArrayList();
        list.addAll(types);
        list.add(Type.var(variable));

        return new Types(list);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String write(Coder coder) {
        StringJoiner builder = new StringJoiner(", ", "<", ">").setEmptyValue("");
        types.forEach(v -> builder.add(coder.use(v)));
        return builder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Types == false) {
            return false;
        }

        Types other = (Types) obj;

        return types.toString().equals(other.types.toString());
    }
}
