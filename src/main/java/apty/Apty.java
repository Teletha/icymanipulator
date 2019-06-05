/*
 * Copyright (C) 2019 icymanipulator Development Team
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          https://opensource.org/licenses/MIT
 */
package apty;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.UnionType;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleAnnotationValueVisitor9;
import javax.lang.model.util.SimpleElementVisitor9;
import javax.lang.model.util.SimpleTypeVisitor9;
import javax.lang.model.util.Types;

public class Apty {

    /** The {@link Object#toString()} method pattern. */
    public static final Predicate<ExecutableElement> ToString = m -> {
        return m.getParameters().size() == 0 && m.getReturnType().toString().equals("java.lang.String") && m.getSimpleName()
                .contentEquals("toString");
    };

    /** The {@link Object#hashCode()} method pattern. */
    public static final Predicate<ExecutableElement> HashCode = m -> {
        return m.getParameters().size() == 0 && m.getReturnType().toString().equals("int") && m.getSimpleName().contentEquals("hashCode");
    };

    /** The {@link Object#equals(Object)} method pattern. */
    public static final Predicate<ExecutableElement> Equals = m -> {
        return m.getParameters().size() == 1 && m.getParameters().get(0).asType().toString().equals("java.lang.Object") && m.getReturnType()
                .toString()
                .equals("boolean") && m.getSimpleName().contentEquals("equals");
    };

    /** The getter pattern. */
    private static final Predicate<ExecutableElement> getter = m -> {
        return m.getParameters().size() == 0 && m.getReturnType().getKind() != TypeKind.VOID;
    };

    /** The setter pattern. */
    private static final Predicate<ExecutableElement> setter = m -> {
        return m.getParameters().size() == 1;
    };

    /** The type utility. */
    private static Types types;

    /** The element utility. */
    private static Elements elements;

    /**
     * Initialize {@link Apty}.
     * 
     * @param process
     */
    static void initialize(ProcessingEnvironment process) {
        types = process.getTypeUtils();
        elements = process.getElementUtils();
    }

    /**
     * Find parent {@link TypeElement}.
     * 
     * @param e
     * @return
     */
    public static TypeElement parent(TypeElement e) {
        return (TypeElement) types.asElement(e.getSuperclass());
    }

    /**
     * Find all declared getter-like methods.
     * 
     * @param e A target type.
     */
    public static List<ExecutableElement> getters(TypeElement e) {
        return methods(e, getter);
    }

    /**
     * Find all declared setter-like methods.
     * 
     * @param e A target type.
     */
    public static List<ExecutableElement> setters(TypeElement e) {
        return methods(e, setter);
    }

    /**
     * Find all declared methods.
     * 
     * @param e A target type.
     * @return
     */
    public static List<ExecutableElement> methods(TypeMirror e) {
        return methods(cast(types.asElement(e), TypeElement.class));
    }

    /**
     * Find all declared methods.
     * 
     * @param e A target type.
     * @return
     */
    public static List<ExecutableElement> methods(TypeElement e) {
        return methods(e, x -> true);
    }

    /**
     * Find the matching declared methods.
     * 
     * @param e A target type.
     * @param filter A including filer.
     * @return
     */
    public static List<ExecutableElement> methods(TypeElement e, Predicate<ExecutableElement> filter) {
        List<ExecutableElement> methods = new ArrayList();
        List<? extends Element> child = e.getEnclosedElements();

        for (Element element : child) {
            if (element.getKind() == ElementKind.METHOD) {
                ExecutableElement executable = (ExecutableElement) element;

                if (filter.test(executable)) {
                    methods.add(executable);
                }
            }
        }
        return methods;
    }

    /**
     * Find the matching declared methods.
     * 
     * @param e A target type.
     * @param filter A including filer.
     * @return
     */
    public static List<ExecutableElement> methodsInHierarchy(TypeElement e, Predicate<ExecutableElement> filter) {
        List<ExecutableElement> methods = new ArrayList();

        while (e != null) {
            List<? extends Element> child = e.getEnclosedElements();

            for (Element element : child) {
                if (element.getKind() == ElementKind.METHOD) {
                    ExecutableElement executable = (ExecutableElement) element;

                    if (filter.test(executable)) {
                        methods.add(executable);
                    }
                }
            }
            e = Apty.parent(e);
        }
        return methods;
    }

    /**
     * Check type equality.
     * 
     * @param type
     * @param element
     * @return
     */
    public static boolean same(TypeMirror type, Element element) {
        return same(type, element.asType());
    }

    /**
     * Check type equality.
     * 
     * @param type
     * @param other
     * @return
     */
    public static boolean same(TypeMirror type, TypeMirror other) {
        return types.isSameType(types.erasure(type), types.erasure(other));
    }

    /**
     * Check type equality.
     * 
     * @param type
     * @param clazz
     * @return
     */
    public static boolean same(TypeMirror type, Class clazz) {
        return same(type, elements.getTypeElement(clazz.getCanonicalName()));
    }

    /**
     * Check whether the specified type is interface or not.
     * 
     * @param e A target type to check.
     * @return A result.
     */
    public static boolean isInterface(TypeElement e) {
        if (e == null) {
            return false;
        }
        return e.getKind() == ElementKind.INTERFACE;
    }

    /**
     * Check whether the specified type is interface or not.
     * 
     * @param type A target type to check.
     * @return A result.
     */
    public static boolean isInterface(DeclaredType type) {
        return isInterface(cast(type));
    }

    /**
     * Check whether the specified type is interface or not.
     * 
     * @param e A target type to check.
     * @return A result.
     */
    public static boolean isNotInterface(TypeElement e) {
        return !isInterface(e);
    }

    /**
     * Check whether the specified type is interface or not.
     * 
     * @param type A target type to check.
     * @return A result.
     */
    public static boolean isNotInterface(DeclaredType type) {
        return !isInterface(type);
    }

    /**
     * Check whether the specified type is enum or not.
     * 
     * @param enumType A target type.
     * @return A result.
     */
    public static Stream<String> enumConstantNames(TypeMirror enumType) {
        Element element = types.asElement(enumType);

        if (element == null) {
            return Stream.empty();
        }

        return element.getEnclosedElements()
                .stream()
                .filter(e -> e.getKind() == ElementKind.ENUM_CONSTANT)
                .map(e -> e.getSimpleName().toString());
    }

    /**
     * Retrieve the documentation comment from the specified {@link Element}.
     * 
     * @param e The target element.
     * @return A documentation comment
     */
    public static String doc(Element e) {
        String doc = elements.getDocComment(e);
        return doc == null ? "" : doc;
    }

    /**
     * Reteieve the variables from the specified base target.
     * 
     * @param target A target base type.
     * @param type A target type which has variable list.
     * @return
     */
    public static List<? extends TypeMirror> variables(TypeElement target, Class type) {
        return variables(target, type, Apty::same);
    }

    /**
     * Reteieve the variables from the specified base target.
     * 
     * @param target A target base type.
     * @param type A target type which has variable list.
     * @return
     */
    public static List<? extends TypeMirror> variables(TypeElement target, DeclaredType type) {
        return variables(target, type, Apty::same);
    }

    /**
     * Reteieve the variables from the specified base target.
     * 
     * @param target A target base type.
     * @param type A target type which has variable list.
     * @return
     */
    public static List<? extends TypeMirror> variables(DeclaredType target, Class type) {
        return variables(cast(target.asElement(), TypeElement.class), type, Apty::same);
    }

    /**
     * Reteieve the variables from the specified base target.
     * 
     * @param target A target base type.
     * @param type A target type which has variable list.
     * @return
     */
    public static List<? extends TypeMirror> variables(DeclaredType target, DeclaredType type) {
        return variables(cast(target.asElement(), TypeElement.class), type, Apty::same);
    }

    /**
     * Reteieve the variables on the specified type from the specified target.
     * 
     * @param <Q>
     * @param target
     * @param type
     * @param equality
     * @return
     */
    private static <Q> List<? extends TypeMirror> variables(TypeElement target, Q type, BiPredicate<TypeMirror, Q> equality) {
        while (target != null) {
            for (TypeMirror interfaceType : target.getInterfaces()) {
                if (equality.test(interfaceType, type)) {
                    return cast(interfaceType, DeclaredType.class).getTypeArguments();
                }
            }
            target = Apty.parent(target);
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * Retrieve annotation info by its type.
     * 
     * @param e An annotated {@link Element}.
     * @param annotationType An annotation type.
     * @return
     */
    public static Optional<AnnotationMirror> annotation(Element e, Class<? extends Annotation> annotationType) {
        String annotationClassName = annotationType.getCanonicalName();
        for (AnnotationMirror annotationMirror : e.getAnnotationMirrors()) {
            TypeElement annotationTypeElement = cast(annotationMirror.getAnnotationType().asElement(), TypeElement.class);

            if (annotationTypeElement.getQualifiedName().contentEquals(annotationClassName)) {
                return Optional.of(annotationMirror);
            }
        }
        return Optional.empty();
    }

    /**
     * Returns the {@link AnnotationMirror}'s map of {@link AnnotationValue} indexed by
     * {@link ExecutableElement}, supplying default values from the annotation if the annotation
     * property has not been set. This is equivalent to
     * {@link Elements#getElementValuesWithDefaults(AnnotationMirror)} but can be called statically
     * without an {@link Elements} instance.
     * <p>
     * The iteration order of elements of the returned map will be the order in which the
     * {@link ExecutableElement}s are defined in {@code annotation}'s
     * {@linkplain AnnotationMirror#getAnnotationType() type}.
     */
    public static Map<ExecutableElement, AnnotationValue> annotationValues(AnnotationMirror annotation) {
        Map<ExecutableElement, AnnotationValue> map = new HashMap();
        Map<? extends ExecutableElement, ? extends AnnotationValue> declaredValues = annotation.getElementValues();
        for (ExecutableElement method : ElementFilter.methodsIn(annotation.getAnnotationType().asElement().getEnclosedElements())) {
            if (declaredValues.containsKey(method)) {
                map.put(method, declaredValues.get(method));
            } else if (method.getDefaultValue() != null) {
                map.put(method, method.getDefaultValue());
            } else {
                throw new UnsupportedOperationException();
            }
        }
        return Collections.unmodifiableMap(map);
    }

    /**
     * Retrieve annotation value by annotation type and value name.
     * 
     * @param e A target element.
     * @param annotationType An annotation type.
     * @param name A value name.
     * @return
     */
    public static Optional<AnnotationValue> annotationValue(Element e, Class<? extends Annotation> annotationType, String name) {
        return annotation(e, annotationType).map(Apty::annotationValues).flatMap(values -> {
            for (Entry<ExecutableElement, AnnotationValue> entry : values.entrySet()) {
                if (entry.getKey().getSimpleName().contentEquals(name)) {
                    return Optional.of(entry.getValue());
                }
            }
            return Optional.empty();
        });
    }

    /**
     * Retrieve annotation class value by annotation type and value name.
     * 
     * @param e A target element.
     * @param annotationType An annotation type.
     * @param name A value name.
     * @return
     */
    public static Optional<DeclaredType> annotationClassValue(Element e, Class<? extends Annotation> annotationType, String name) {
        return annotationValue(e, annotationType, name).map(value -> cast(value, DeclaredType.class));
    }

    public static boolean check(Element target, Predicate<TypeMirror> condition) {
        return condition.test(target.asType());
    }

    /**
     * Create implementation validator.
     * 
     * @param type A type to implmenet or extend.
     * @return A new validator.
     */
    public static Predicate<TypeMirror> implement(Class type) {
        return implement(cast(type));
    }

    /**
     * Create implementation validator.
     * 
     * @param type A type to implmenet or extend.
     * @return A new validator.
     */
    public static Predicate<TypeMirror> implement(TypeElement type) {
        return implement(type.asType());
    }

    /**
     * Create implementation validator.
     * 
     * @param type A type to implmenet or extend.
     * @return A new validator.
     */
    public static Predicate<TypeMirror> implement(TypeMirror type) {
        return target -> types.isSubtype(target, types.erasure(type));
    }

    /**
     * Cast {@link DeclaredType} to {@link TypeElement}.
     * 
     * @param type A type to cast.
     * @return A casted type.
     */
    public static TypeElement cast(DeclaredType type) {
        return cast(type.asElement(), TypeElement.class);
    }

    /**
     * Cast {@link Class} to {@link TypeElement}.
     * 
     * @param type A type to cast.
     * @return A casted type.
     */
    private static TypeElement cast(Class type) {
        return elements.getTypeElement(type.getName());
    }

    /**
     * Cast to the specified type safely.
     * 
     * @param <T>
     * @param e
     * @param type
     * @return
     */
    private static <T extends Element> T cast(Element e, Class<T> type) {
        return e.accept(ElementCaster.by(type), null);
    }

    /**
     * 
     */
    @SuppressWarnings("unused")
    private abstract static class ElementCaster<T> extends SimpleElementVisitor9<T, Void> {

        private static final Map<Class, ElementCaster> casters = new HashMap();

        /**
         * Find {@link ElementCaster} by type.
         * 
         * @param <T>
         * @param type
         * @return
         */
        private static final <T> ElementCaster<T> by(Class<T> type) {
            return casters.get(type);
        }

        /** The cast type. */
        private final Class<T> type;

        /**
         * @param type
         */
        private ElementCaster(Class type) {
            this.type = type;
            casters.put(type, this);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected T defaultAction(Element e, Void p) {
            throw new Fail(e, "Can't cast to " + type.getSimpleName() + ".");
        }

        /** converter */
        private static final ElementCaster TYPE = new ElementCaster<>(TypeElement.class) {
            @Override
            public TypeElement visitType(TypeElement e, Void p) {
                return e;
            }
        };

        /** converter */
        private static final ElementCaster TYPE_PARAMETER = new ElementCaster<>(TypeParameterElement.class) {
            @Override
            public TypeParameterElement visitTypeParameter(TypeParameterElement e, Void p) {
                return e;
            }
        };

        /** converter */
        private static final ElementCaster PACKAGE = new ElementCaster<>(PackageElement.class) {
            @Override
            public PackageElement visitPackage(PackageElement e, Void p) {
                return e;
            }
        };

        /** converter */
        private static final ElementCaster EXECUTABLE = new ElementCaster<>(ExecutableElement.class) {
            @Override
            public ExecutableElement visitExecutable(ExecutableElement e, Void p) {
                return e;
            }
        };

        /** converter */
        private static final ElementCaster VARIABLE = new ElementCaster<>(VariableElement.class) {
            @Override
            public VariableElement visitVariable(VariableElement e, Void p) {
                return e;
            }
        };
    }

    /**
     * Cast to the specified type safely.
     * 
     * @param <T>
     * @param e
     * @param type
     * @return
     */
    private static <T extends TypeMirror> T cast(TypeMirror e, Class<T> type) {
        return e.accept(TypeCaster.by(type), null);
    }

    /**
     * 
     */
    @SuppressWarnings("unused")
    private abstract static class TypeCaster<T> extends SimpleTypeVisitor9<T, Void> {

        private static final Map<Class, TypeCaster> casters = new HashMap();

        /**
         * Find {@link TypeCaster} by type.
         * 
         * @param <T>
         * @param type
         * @return
         */
        private static final <T> TypeCaster<T> by(Class<T> type) {
            return casters.get(type);
        }

        /** The cast type. */
        private final Class<T> type;

        /**
         * @param type
         */
        private TypeCaster(Class type) {
            this.type = type;
            casters.put(type, this);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected T defaultAction(TypeMirror e, Void p) {
            throw new Fail(types.asElement(e), "Can't cast to " + type.getSimpleName() + ".");
        }

        /** converter */
        private static final TypeCaster DECLARED = new TypeCaster<>(DeclaredType.class) {
            @Override
            public DeclaredType visitDeclared(DeclaredType t, Void p) {
                return t;
            }
        };

        /** converter */
        private static final TypeCaster ARRAY = new TypeCaster<>(ArrayType.class) {
            @Override
            public Object visitArray(ArrayType t, Void p) {
                return t;
            }
        };

        /** converter */
        private static final TypeCaster ERROR = new TypeCaster<>(ErrorType.class) {
            @Override
            public Object visitError(javax.lang.model.type.ErrorType t, Void p) {
                return t;
            }
        };

        /** converter */
        private static final TypeCaster EXECUTABLE = new TypeCaster<>(ExecutableType.class) {
            @Override
            public ExecutableType visitExecutable(ExecutableType t, Void p) {
                return t;
            }
        };

        /** converter */
        private static final TypeCaster INTERSECTION = new TypeCaster<>(IntersectionType.class) {
            @Override
            public IntersectionType visitIntersection(IntersectionType t, Void p) {
                return t;
            }
        };

        /** converter */
        private static final TypeCaster NO = new TypeCaster<>(NoType.class) {
            @Override
            public NoType visitNoType(NoType t, Void p) {
                return t;
            }
        };

        /** converter */
        private static final TypeCaster NULL = new TypeCaster<>(NullType.class) {
            @Override
            public NullType visitNull(NullType t, Void p) {
                return t;
            }
        };

        /** converter */
        private static final TypeCaster PRIMITIVE = new TypeCaster<>(PrimitiveType.class) {
            @Override
            public PrimitiveType visitPrimitive(PrimitiveType t, Void p) {
                return t;
            }
        };

        /** converter */
        private static final TypeCaster VARIABLE = new TypeCaster<>(TypeVariable.class) {
            @Override
            public TypeVariable visitTypeVariable(TypeVariable t, Void p) {
                return t;
            }
        };

        /** converter */
        private static final TypeCaster UNION = new TypeCaster<>(UnionType.class) {
            @Override
            public UnionType visitUnion(UnionType t, Void p) {
                return t;
            }
        };

        /** converter */
        private static final TypeCaster WILDCARD = new TypeCaster<>(WildcardType.class) {
            @Override
            public WildcardType visitWildcard(WildcardType t, Void p) {
                return t;
            }
        };
    }

    /**
     * Cast to the specified type safely.
     * 
     * @param <T>
     * @param e
     * @param type
     * @return
     */
    private static <T> T cast(AnnotationValue e, Class<T> type) {
        return e.accept(AnnotationValueCaster.by(type), null);
    }

    /**
     * 
     */
    @SuppressWarnings("unused")
    private static abstract class AnnotationValueCaster<T> extends SimpleAnnotationValueVisitor9<T, Void> {

        private static final Map<Class, AnnotationValueCaster> casters = new HashMap();

        /**
         * Find {@link AnnotationValueCaster} by type.
         * 
         * @param <T>
         * @param type
         * @return
         */
        private static final <T> AnnotationValueCaster<T> by(Class<T> type) {
            return casters.get(type);
        }

        /** The cast type. */
        private final Class<T> type;

        /**
         * @param type
         */
        private AnnotationValueCaster(Class type) {
            this.type = type;
            casters.put(type, this);
        }

        /** converter */
        private static final AnnotationValueCaster TYPE = new AnnotationValueCaster<>(DeclaredType.class) {
            @Override
            public DeclaredType visitType(TypeMirror t, Void p) {
                return cast(t, DeclaredType.class);
            }
        };

        /** converter */
        private static final AnnotationValueCaster TYPES = new AnnotationValueCaster<>(DeclaredType[].class) {

            /**
             * {@inheritDoc}
             */
            @Override
            public DeclaredType[] visitArray(List<? extends AnnotationValue> vals, Void p) {
                List<DeclaredType> list = new ArrayList();
                for (AnnotationValue value : vals) {
                    list.add(cast(value, DeclaredType.class));
                }
                return list.toArray(DeclaredType[]::new);
            }
        };
    }
}
