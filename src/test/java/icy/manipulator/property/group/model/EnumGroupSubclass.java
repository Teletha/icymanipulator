package icy.manipulator.property.group.model;

import icy.manipulator.property.group.model.EnumGroup;
import icy.manipulator.property.group.model.EnumGroupModel.Color;
import icy.manipulator.property.group.model.EnumGroupSubclass;
import icy.manipulator.property.group.model.EnumGroupSubclassModel;
import javax.annotation.processing.Generated;

/**
 * Generated model for {@link EnumGroupSubclassModel}.
 */
@Generated("Icy Manipulator")
public abstract class EnumGroupSubclass extends EnumGroupSubclassModel {

    /**
     * HIDE CONSTRUCTOR
     */
    protected EnumGroupSubclass() {
    }

    /** The singleton builder. */
    public static final  Ìnstantiator<?> with = new Ìnstantiator();

    /**
     * Namespace for {@link EnumGroupSubclass}  builder methods.
     */
    public static final class Ìnstantiator<Self extends EnumGroupSubclass & ÅssignableÅrbitrary<Self>> {

        /**
         * Create new {@link EnumGroupSubclass} with the specified color property.
         * 
         * @return The next assignable model.
         */
        public final Self color(Color color, int size) {
            Åssignable o = new Åssignable();
            o.color(color);
            o.size(size);
            return (Self) o;
        }

        /**
         * Create new {@link EnumGroupSubclass} with the specified color property.
         * 
         * @return The next assignable model.
         */
        public final Self red(int size) {
            Åssignable o = new Åssignable();
            o.red();
            o.size(size);
            return (Self) o;
        }

        /**
         * Create new {@link EnumGroupSubclass} with the specified color property.
         * 
         * @return The next assignable model.
         */
        public final Self blue(int size) {
            Åssignable o = new Åssignable();
            o.blue();
            o.size(size);
            return (Self) o;
        }

        /**
         * Create new {@link EnumGroupSubclass} with the specified color property.
         * 
         * @return The next assignable model.
         */
        public final Self green(int size) {
            Åssignable o = new Åssignable();
            o.green();
            o.size(size);
            return (Self) o;
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableÅrbitrary<Next extends EnumGroupSubclass> {
    }

    /**
     * Internal aggregated API.
     */
    protected static interface ÅssignableAll extends EnumGroup.ÅssignableAll {
    }

    /**
     * Mutable Model.
     */
    private static final class Åssignable extends EnumGroupSubclass implements ÅssignableAll, ÅssignableÅrbitrary {
    }

    /**
     * The identifier for properties.
     */
    static final class My {
    }
}
