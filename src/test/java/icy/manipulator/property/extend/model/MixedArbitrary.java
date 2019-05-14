package icy.manipulator.property.extend.model;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import javax.annotation.processing.Generated;

/**
 * Generated model for {@link MixedArbitraryModel}.
 */
@Generated("Icy Manipulator")
public abstract class MixedArbitrary extends MixedArbitraryModel {

    /**
     * Create special property updater.
     *
     * @param name A target property name.
     * @return A special property updater.
     */
    private static final MethodHandle updater(String name)  {
        try {
            Field field = MixedArbitrary.class.getDeclaredField(name);
            field.setAccessible(true);
            return MethodHandles.lookup().unreflectSetter(field);
        } catch (Throwable e) {
            throw new Error(e);
        }
    }

    /** The final property updater. */
    private static final MethodHandle optionZipUpdater = updater("optionZip");

    /** The exposed property. */
    public final String optionZip;

    /**
     * HIDE CONSTRUCTOR
     */
    protected MixedArbitrary() {
        this.optionZip = super.optionZip();
    }

    /**
     * Return the optionZip property.
     *
     * @return A value of optionZip property.
     */
    @Override
    public final String optionZip() {
        return this.optionZip;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of optionZip property.
     */
    @SuppressWarnings("unused")
    private final String getOptionZip() {
        return this.optionZip;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of optionZip property to assign.
     */
    @SuppressWarnings("unused")
    private void setOptionZip(String value) {
        ((ÅssignableÅrbitrary) this).optionZip(value);
    }

    /** The singleton builder. */
    public static final  Ìnstantiator<?> with = new Ìnstantiator();

    /**
     * Namespace for {@link MixedArbitrary}  builder methods.
     */
    public static final class Ìnstantiator<Self extends MixedArbitrary & ÅssignableÅrbitrary<Self>> {

        /**
         * Create new {@link MixedArbitrary} with the specified name property.
         * 
         * @return The next assignable model.
         */
        public final <T extends ÅssignableAge<Self>> T name(String string) {
            Åssignable o = new Åssignable();
            o.name(string);
            return (T) o;
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableÅrbitrary<Next extends MixedArbitrary> extends icy.manipulator.property.model.Mixed.ÅssignableÅrbitrary<Next> {

        /**
         * Assign optionZip property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next optionZip(String value) {
            try {
                optionZipUpdater.invoke(this, value);
            } catch (Throwable e) {
                throw new Error(e);
            }
            return (Next) this;
        }
    }

    /**
     * Internal aggregated API.
     */
    protected static interface ÅssignableAll extends icy.manipulator.property.model.Mixed.ÅssignableAll {
    }

    /**
     * Mutable Model.
     */
    private static final class Åssignable extends MixedArbitrary implements ÅssignableAll, ÅssignableÅrbitrary {
    }

    /**
     * The identifier for properties.
     */
    static final class My {
        static final String OptionZip = "optionZip";
    }
}
