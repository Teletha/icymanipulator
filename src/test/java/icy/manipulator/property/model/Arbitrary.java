package icy.manipulator.property.model;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import javax.annotation.processing.Generated;

/**
 * Generated model for {@link ArbitraryModel}.
 */
@Generated("Icy Manipulator")
public abstract class Arbitrary extends ArbitraryModel {

    /**
     * Create special property updater.
     *
     * @param name A target property name.
     * @return A special property updater.
     */
    private static final MethodHandle updater(String name)  {
        try {
            Field field = Arbitrary.class.getDeclaredField(name);
            field.setAccessible(true);
            return MethodHandles.lookup().unreflectSetter(field);
        } catch (Throwable e) {
            throw new Error(e);
        }
    }

    /** The final property updater. */
    private static final MethodHandle optionNumUpdater = updater("optionNum");

    /** The final property updater. */
    private static final MethodHandle optionCommentUpdater = updater("optionComment");

    /** The exposed property. */
    public final int optionNum;

    /** The exposed property. */
    public final String optionComment;

    /**
     * HIDE CONSTRUCTOR
     */
    protected Arbitrary() {
        this.optionNum = super.optionNum();
        this.optionComment = super.optionComment();
    }

    /**
     * Return the optionNum property.
     *
     * @return A value of optionNum property.
     */
    @Override
    public final int optionNum() {
        return this.optionNum;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of optionNum property.
     */
    @SuppressWarnings("unused")
    private final int getOptionNum() {
        return this.optionNum;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of optionNum property to assign.
     */
    @SuppressWarnings("unused")
    private void setOptionNum(int value) {
        ((ÅssignableÅrbitrary) this).optionNum(value);
    }

    /**
     * Return the optionComment property.
     *
     * @return A value of optionComment property.
     */
    @Override
    public final String optionComment() {
        return this.optionComment;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of optionComment property.
     */
    @SuppressWarnings("unused")
    private final String getOptionComment() {
        return this.optionComment;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of optionComment property to assign.
     */
    @SuppressWarnings("unused")
    private void setOptionComment(String value) {
        ((ÅssignableÅrbitrary) this).optionComment(value);
    }

    /** The singleton builder. */
    public static final  Ìnstantiator<?> with = new Ìnstantiator();

    /**
     * Namespace for {@link Arbitrary}  builder methods.
     */
    public static final class Ìnstantiator<Self extends Arbitrary & ÅssignableÅrbitrary<Self>> {

        /**
         * Create initialized {@link Arbitrary}.
         */
        public final Self create() {
            return (Self) new Åssignable();
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableÅrbitrary<Next extends Arbitrary> {

        /**
         * Assign optionNum property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next optionNum(int value) {
            try {
                optionNumUpdater.invoke(this, value);
            } catch (Throwable e) {
                throw new Error(e);
            }
            return (Next) this;
        }

        /**
         * Assign optionComment property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next optionComment(String value) {
            try {
                optionCommentUpdater.invoke(this, value);
            } catch (Throwable e) {
                throw new Error(e);
            }
            return (Next) this;
        }
    }

    /**
     * Internal aggregated API.
     */
    protected static interface ÅssignableAll {
    }

    /**
     * Mutable Model.
     */
    private static final class Åssignable extends Arbitrary implements ÅssignableAll, ÅssignableÅrbitrary {
    }

    /**
     * The identifier for properties.
     */
    static final class My {
        static final String OptionNum = "optionNum";
        static final String OptionComment = "optionComment";
    }
}
