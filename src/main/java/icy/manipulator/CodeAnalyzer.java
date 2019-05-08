/*
 * Copyright (C) 2019 Nameless Production Committee
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://opensource.org/licenses/mit-license.php
 */
package icy.manipulator;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import javax.annotation.processing.Generated;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;

import icy.manipulator.Icy.Overload;

class CodeAnalyzer implements ElementVisitor<CodeAnalyzer, VariableElement> {

    /** The prefix of assignable type. */
    static final String Assignable = "Åssignable";

    /** The instantiator class name. */
    private static final String Instantiator = "Ìnstantiator";

    /** The next type parameter. */
    private static final String Next = "Next";

    /** The configuratino interface name for arbitrary perperties. */
    private static final String ArbitraryInterface = Assignable + "Årbitrary";

    /** The root element of the model. */
    private final Element root;

    /** The {@link Icy} info on the model class. */
    private final Icy icy;

    /** The fully qualified model class name. */
    private Type model;

    /** The fully qualified generated class name. */
    Type clazz;

    /** The property list. */
    final List<Property> properties = new ArrayList();

    /** The required properties. */
    private final List<Property> required = new ArrayList();

    /** The arbitrary properties. */
    private final List<Property> arbitrary = new ArrayList();

    /** The overload method holder. */
    private final List<Method> overloads = new ArrayList();

    /** The overload method for each property */
    private final PropetyInfo<Method> overloadForProperty = new PropetyInfo();

    private Coder code = new Coder(IcyManipulator.importer);

    private final Messager messager;

    /**
     * Create code analyzer.
     * 
     * @param root
     * @param messager
     */
    CodeAnalyzer(Element root, Messager messager) {
        this.root = root;
        this.icy = root.getAnnotation(Icy.class);
        this.messager = messager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CodeAnalyzer visit(Element e, VariableElement p) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CodeAnalyzer visit(Element e) {
        return visit(e, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CodeAnalyzer visitPackage(PackageElement e, VariableElement p) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CodeAnalyzer visitType(TypeElement e, VariableElement p) {
        switch (e.getKind()) {
        case CLASS:
            if (model == null) {
                String name = e.getQualifiedName().toString();
                DeclaredType declared = (DeclaredType) e.asType();
                List<? extends TypeMirror> variables = declared.getTypeArguments();

                model = new Type(name, variables);
                clazz = new Type(name.replaceAll(IcyManipulator.ModelDefinitionSuffix + "$", ""), variables);
            }
            break;

        default:
            break;
        }

        for (Element sub : e.getEnclosedElements()) {
            sub.accept(this, p);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CodeAnalyzer visitVariable(VariableElement e, VariableElement p) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CodeAnalyzer visitExecutable(ExecutableElement e, VariableElement z) {
        if (e.getKind() == ElementKind.METHOD) {
            // Derive derive = e.getAnnotation(Icy.Derive.class);
            //
            // if (derive != null && isDeriveMethod(e)) {
            // Arrays.stream(derive.to()).map(this::findPropertyByName).flatMap(Optional::stream).forEach(p
            // -> p.isDerived = true);
            // Arrays.stream(derive.by())
            // .map(this::findPropertyByName)
            // .flatMap(Optional::stream)
            // .forEach(p -> p.derive = e.getSimpleName().toString());
            // }

            createAsProperty(e);

            // collect overload methods
            Overload overload = e.getAnnotation(Icy.Overload.class);
            if (overload != null) overloads.add(new Method(e));
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CodeAnalyzer visitTypeParameter(TypeParameterElement e, VariableElement p) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CodeAnalyzer visitUnknown(Element e, VariableElement p) {
        return this;
    }

    /**
     * Prepare to analyze.
     */
    void prepare() {
        properties.addAll(required);
        properties.addAll(arbitrary);

        if (required.size() != 0) {
            for (int i = 0; i < required.size() - 1; i++) {
                required.get(i).nextProperty = required.get(i + 1);
                required.get(i).next = required.get(i + 1).assignableInterfaceName();
            }
            required.get(required.size() - 1).next = self();
        }

        for (Property property : arbitrary) {
            property.next = self();
        }

        validateOverload();
    }

    /**
     * Validate overload method.
     */
    private void validateOverload() {
        for (Method method : overloads) {
            Overload overload = method.getAnnotation(Overload.class);
            String targetProperty = overload.value().isEmpty() ? method.name : overload.value();

            Property property = findPropertyByName(targetProperty);

            if (property == null) {
                error("Although you specify the property [" + targetProperty + "], it is not found. Specify the correct property name.", method.element);
                return;
            }

            if (!method.returnType.equals(property.type)) {
                error("Although the property [" + targetProperty + "] type is " + method.returnType + ", overload method [" + method + "] returns " + method.returnType + ".", method.element);
                return;
            }
            overloadForProperty.add(property, method);
        }
    }

    /**
     * Generate source code.
     * 
     * @return
     */
    String defineCode() {
        String visibility = icy.packagePrivate() ? "" : "public ";

        code.write("/**");
        code.write(" * Generated model for {@link ", model, "}.");
        code.write(" */");
        code.write("@", Generated.class, "(\"Icy Manipulator\")");
        code.write(visibility, "abstract class ", clazz, " extends ", model, () -> {
            if (!overloads.isEmpty()) {
                defineMethodInvokerBuilder();
                defineOverloadMethodInvoker();
            }
            defineFiledUpdaterBuilder();
            defineFieldUpdater();
            defineField();
            defineConstructor();
            defineAccessors();
            defineBuilder();
            defineAssignableInterfaces();
            defineMutableClass();
            definePropertyEnum();
        });
        return code.toCode();
    }

    /**
     * Define query method for property updater.
     */
    private void defineMethodInvokerBuilder() {
        code.write();
        code.write("/**");
        code.write(" * Create special method invoker.");
        code.write(" *");
        code.write(" * @param name A target method name.");
        code.write(" * @param parameterTypes A list of method parameter types.");
        code.write(" * @return A special method invoker.");
        code.write(" */");
        code.write("private static final ", MethodHandle.class, " updater(String name, Class... parameterTypes) ", () -> {
            code.writeTry(() -> {
                code.write(java.lang.reflect.Method.class, " method = ", model, ".class.getDeclaredMethod(name, parameterTypes);");
                code.write("method.setAccessible(true);");
                code.write("return ", MethodHandles.class, ".lookup().unreflect(method);");
            }, Throwable.class, e -> {
                code.write("throw new Error(", e, ");");
            });
        });
    }

    /**
     * Define property updater.
     */
    private void defineOverloadMethodInvoker() {
        for (Property property : properties) {
            for (Method method : overloadForProperty.get(property)) {
                StringJoiner types = new StringJoiner(", ", ", ", "").setEmptyValue("");
                method.paramTypes.forEach(t -> types.add(code.use(t) + ".class"));

                code.write();
                code.write("/** The overload method invoker. */");
                code.write("private static final ", MethodHandle.class, " ", method.id, "= updater(\"", method.name, "\"", types, ");");
            }
        }
    }

    /**
     * Define query method for property updater.
     */
    private void defineFiledUpdaterBuilder() {
        code.write();
        code.write("/**");
        code.write(" * Create special property updater.");
        code.write(" *");
        code.write(" * @param name A target property name.");
        code.write(" * @return A special property updater.");
        code.write(" */");
        code.write("private static final ", MethodHandle.class, " updater(String name) ", () -> {
            code.writeTry(() -> {
                code.write(Field.class, " field = ", clazz, ".class.getDeclaredField(name);");
                code.write("field.setAccessible(true);");
                code.write("return ", MethodHandles.class, ".lookup().unreflectSetter(field);");
            }, Throwable.class, e -> {
                code.write("throw new Error(", e, ");");
            });
        });
    }

    /**
     * Define property updater.
     */
    private void defineFieldUpdater() {
        for (Property property : properties) {
            if (property.isFinal) {
                code.write();
                code.write("/** The final property updater. */");
                code.write("private static final ", MethodHandle.class, " ", property.name, "Updater = updater(\"", property.name, "\");");
            }
        }
    }

    /**
     * Define property field.
     */
    private void defineField() {
        for (Property property : properties) {
            code.write();
            code.write("/** The exposed property. */");
            code.write("public final ", property.type, " ", property.name, ";");
        }
    }

    /**
     * Define constructor.
     */
    private void defineConstructor() {
        code.write();
        code.write("/**");
        code.write(" * HIDE CONSTRUCTOR");
        code.write(" */");
        code.write("protected ", clazz, "()", () -> {
            // initialize field
            for (Property p : properties) {
                code.write("this.", p.name, " = ", (p.isArbitrary ? "super." + p.name + "()" : p.type.defaultValue()), ";");
            }
        });
    }

    /**
     * Define property getter methods.
     */
    private void defineAccessors() {
        for (Property property : properties) {
            // Exposed getter
            code.write();
            code.write("/**");
            code.write(" * Retrieve ", property.name, " property.");
            code.write(" */");
            code.write("@Override");
            code.write("public final ", property.type, " ", property.name, "()", () -> {
                code.write("return this.", property.name, ";");
            });

            // Hidden classic getter
            code.write();
            code.write("/**");
            code.write(" * Provide classic getter API.");
            code.write(" */");
            code.write("@SuppressWarnings(\"unused\")");
            code.write("private final ", property.type, " get", property.capitalizeName(), "()", () -> {
                code.write("return this.", property.name, ";");
            });

            // Hidden classic setter
            code.write();
            code.write("/**");
            code.write(" * Provide classic setter API.");
            code.write(" */");
            code.write("private final void set", property.capitalizeName(), "(", property.type, " value)", () -> {
                if (property.isFinal == false) {
                    code.write("this.", property.name, " = value;");
                    if (property.derive != null) code.write("super.", property.derive, "(this);");
                } else {
                    code.writeTry(() -> {
                        code.write(property.name, "Updater.invoke(this, value);");
                        if (property.derive != null) code.write("super.", property.derive, "(this);");
                    }, Throwable.class, e -> {
                        code.write("throw new Error(", e, ");");
                    });
                }
            });
        }
    }

    // public static final class AA<X extends Default & ÅssignableÅrbitrary<X>> {
    //
    // public X create() {
    // return (X) new Åssignable()
    // }
    // }

    /**
     * Defien model builder methods.
     */
    private void defineBuilder() {
        String builder = icy.builder();

        code.write();
        code.write("/** The singleton builder. */");
        code.write("public static final ", Instantiator, "<?> ", builder, " = new ", Instantiator, "();");

        code.write();
        code.write("/**");
        code.write(" * Builder namespace for {@link ", clazz, "}.");
        code.write(" */");
        code.write("public static final class ", Instantiator, "<Self extends ", required.isEmpty() ? self()
                : required.get(0).nextAssignable(self()), ">", () -> {
                    code.write();

                    if (required.isEmpty()) {
                        code.write("/**");
                        code.write(" * Create uninitialized {@link ", clazz, "}.");
                        code.write(" */");
                        code.write("public final Self create()", () -> {
                            code.write("return (Self) new ", Assignable, "();");
                        });
                    } else {
                        Property p = required.get(0);
                        code.write("/** Create Uninitialized {@link ", clazz, "}. */");
                        code.write("public final Self ", p.name, "(", p.type, " value)", () -> {
                            code.write("return (Self) new ", Assignable, "().", p.name, "(value);");
                        });
                        for (Method method : overloadForProperty.get(p)) {
                            code.write();
                            code.write("/** Create Uninitialized {@link ", clazz, "}. */");
                            code.write("public final Self ", method, () -> {
                                code.write("return (Self) new ", Assignable, "().", method.name, "(", method.paramNames, ");");
                            });
                        }
                    }
                });

    }

    // /**
    // * {@inheritDoc}
    // */
    // @Override
    // public <T extends ÅssignableDate> T sizeByText(String number) {
    // Åssignable insntant = new Åssignable(null);
    // try {
    // insntant.size((BigDecimal) sizeByTextjavalangString.invoke(insntant, number));
    // } catch (Throwable e) {
    // throw new Error(e);
    // }
    // return (T) insntant;
    // }

    /**
     * Define mutable model class.
     */
    private void defineMutableClass() {
        // compute interfaces
        StringJoiner interfaces = new StringJoiner(", ", " implements ", "");
        required.forEach(e -> interfaces.add(e.assignableInterfaceName()));
        if (!arbitrary.isEmpty()) interfaces.add(ArbitraryInterface + "<" + Assignable + ">");

        code.write();
        code.write("/**");
        code.write(" * Mutable Model.");
        code.write(" */");
        code.write("private static final class ", Assignable, clazz.variable, " extends ", clazz, interfaces, () -> {
        });
    }

    /**
     * Define assignable interfaces.
     */
    private void defineAssignableInterfaces() {
        for (Property p : required) {
            code.write();
            code.write("/**");
            code.write(" * Property assignment API.");
            code.write(" */");
            code.write("public static interface ", p.assignableInterfaceName(), "<Next>", () -> {
                for (Method method : overloadForProperty.get(p)) {
                    code.write();
                    code.write("/**");
                    code.write(" * The overload setter.");
                    code.write(" */");
                    code.write("default Next ", method, () -> {
                        code.writeTry(() -> {
                            code.write("return ", p.name, "((", method.returnType, ") ", method.id, ".invoke(this, ", method.paramNames, "));");
                        }, Throwable.class, e -> {
                            code.write("throw new Error(", e, ");");
                        });
                    });
                }
                code.write();
                code.write("/**");
                code.write(" * The setter.");
                code.write(" */");
                code.write("default Next ", p.name, "(", p.type, " value)", () -> {
                    code.write("((", clazz, ") this).set", p.capitalizeName(), "(value);");
                    code.write("return (Next) this;");
                });
            });
        }

        if (arbitrary.size() != 0) {
            code.write();
            code.write("/**");
            code.write(" * Property assignment API.");
            code.write(" */");
            code.write("public static interface ", ArbitraryInterface, "<Next extends ", clazz, ">", () -> {
                for (Property property : arbitrary) {
                    code.write();
                    code.write("/**");
                    code.write(" * Property assignment API.");
                    code.write(" */");
                    code.write("default Next ", property.name, "(", property.type, " value)", () -> {
                        code.write("((", clazz, ") this).set", property.capitalizeName(), "(value);");
                        code.write("return (Next) this;");
                    });
                }
            });
        }
    }

    /**
     * Define property identity.
     */
    private void definePropertyEnum() {
        code.write();
        code.write("/**");
        code.write(" * The identifier for properties.");
        code.write(" */");
        code.write("static final class My", () -> {
            for (Property property : properties) {
                code.write("static final String ", property.capitalizeName(), " = \"", property.name, "\";");
            }
        });
    }

    /** The error existence state. */
    boolean hasError;

    /**
     * <p>
     * Notify error.
     * </p>
     * 
     * @param message
     * @param position
     */
    void error(String message, Element position) {
        if (message != null && position != null) {
            hasError = true;
            messager.printMessage(Kind.ERROR, message, position);
        }
    }

    private Property findPropertyByName(String name) {
        for (Property property : properties) {
            if (property.name.equals(name)) {
                return property;
            }
        }
        return null;
    }

    private boolean isDeriveMethod(ExecutableElement method) {
        ExecutableType exe = (ExecutableType) method.asType();

        List<? extends TypeMirror> parameters = exe.getParameterTypes();

        if (parameters.size() != 1) {
            return false;
        }

        Type modelType = Type.of(parameters.get(0));

        if (!modelType.equals(clazz)) {
            return false;
        }
        return true;
    }

    /**
     * Check property declaring method.
     * 
     * @param method
     */
    private void createAsProperty(ExecutableElement method) {
        // require annotation
        icy.manipulator.Icy.Property annotation = method.getAnnotation(Icy.Property.class);

        if (annotation == null) {
            return;
        }

        // require no parameter
        if (method.getParameters().size() != 0) {
            error("Property declaring method must have no parameter.", method);
            return;
        }

        Type returnType = Type.of(method.getReturnType());

        if (returnType.className.equalsIgnoreCase("void")) {
            error("Property declaring method must return something.", method);
            return;
        }

        Property property = new Property(returnType, method.getSimpleName().toString(), true);
        property.isArbitrary = !method.getModifiers().contains(Modifier.ABSTRACT);

        if (property.isArbitrary) {
            arbitrary.add(property);
        } else {
            required.add(property);
        }
    }

    private String self() {
        return arbitrary.isEmpty() ? clazz.className : clazz.className + " & " + ArbitraryInterface + "<Self>";
    }
}