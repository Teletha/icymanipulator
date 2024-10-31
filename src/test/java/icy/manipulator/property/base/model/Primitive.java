package icy.manipulator.property.base.model;

import icy.manipulator.property.base.model.Primitive;
import java.lang.Override;
import java.lang.StringBuilder;
import java.lang.Throwable;
import java.lang.UnsupportedOperationException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Generated model for {@link PrimitiveModel}.
 * 
 * @see <a href="https://github.com/teletha/icymanipulator">Icy Manipulator (Code Generator)</a>
 */
public class Primitive extends PrimitiveModel {

    /**
     * Deceive complier that the specified checked exception is unchecked exception.
     *
     * @param <T> A dummy type for {@link RuntimeException}.
     * @param throwable Any error.
     * @return A runtime error.
     * @throws T Dummy error to deceive compiler.
     */
    private static final <T extends Throwable> T quiet(Throwable throwable) throws T {
        throw (T) throwable;
    }

    /**
     * Create special property updater.
     *
     * @param name A target property name.
     * @return A special property updater.
     */
    private static final MethodHandle updater(String name)  {
        try {
            Field field = Primitive.class.getDeclaredField(name);
            field.setAccessible(true);
            return MethodHandles.lookup().unreflectSetter(field);
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /** The property holder.*/
    // A primitive property is hidden coz native-image builder can't cheat assigning to final field.
    // If you want expose as public-final field, you must use the wrapper type instead of primitive type.
    protected int intX;

    /** The property holder.*/
    // A primitive property is hidden coz native-image builder can't cheat assigning to final field.
    // If you want expose as public-final field, you must use the wrapper type instead of primitive type.
    protected long longX;

    /** The property holder.*/
    // A primitive property is hidden coz native-image builder can't cheat assigning to final field.
    // If you want expose as public-final field, you must use the wrapper type instead of primitive type.
    protected float floatX;

    /** The property holder.*/
    // A primitive property is hidden coz native-image builder can't cheat assigning to final field.
    // If you want expose as public-final field, you must use the wrapper type instead of primitive type.
    protected double doubleX;

    /** The property holder.*/
    // A primitive property is hidden coz native-image builder can't cheat assigning to final field.
    // If you want expose as public-final field, you must use the wrapper type instead of primitive type.
    protected byte byteX;

    /** The property holder.*/
    // A primitive property is hidden coz native-image builder can't cheat assigning to final field.
    // If you want expose as public-final field, you must use the wrapper type instead of primitive type.
    protected short shortX;

    /** The property holder.*/
    // A primitive property is hidden coz native-image builder can't cheat assigning to final field.
    // If you want expose as public-final field, you must use the wrapper type instead of primitive type.
    protected char charX;

    /** The property holder.*/
    // A primitive property is hidden coz native-image builder can't cheat assigning to final field.
    // If you want expose as public-final field, you must use the wrapper type instead of primitive type.
    protected boolean booleanX;

    /**
     * HIDE CONSTRUCTOR
     */
    protected Primitive() {
        this.intX = 0;
        this.longX = 0L;
        this.floatX = 0;
        this.doubleX = 0D;
        this.byteX = 0;
        this.shortX = 0;
        this.charX = ' ';
        this.booleanX = false;
    }

    /**
     * Return the intX property.
     *
     * @return A value of intX property.
     */
    @Override
    public final int intX() {
        return this.intX;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of intX property.
     */
    @SuppressWarnings("unused")
    private final int getIntX() {
        return this.intX;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of intX property to assign.
     */
    private final void setIntX(int value) {
        try {
            this.intX = (int) value;
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * Return the longX property.
     *
     * @return A value of longX property.
     */
    @Override
    public final long longX() {
        return this.longX;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of longX property.
     */
    @SuppressWarnings("unused")
    private final long getLongX() {
        return this.longX;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of longX property to assign.
     */
    private final void setLongX(long value) {
        try {
            this.longX = (long) value;
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * Return the floatX property.
     *
     * @return A value of floatX property.
     */
    @Override
    public final float floatX() {
        return this.floatX;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of floatX property.
     */
    @SuppressWarnings("unused")
    private final float getFloatX() {
        return this.floatX;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of floatX property to assign.
     */
    private final void setFloatX(float value) {
        try {
            this.floatX = (float) value;
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * Return the doubleX property.
     *
     * @return A value of doubleX property.
     */
    @Override
    public final double doubleX() {
        return this.doubleX;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of doubleX property.
     */
    @SuppressWarnings("unused")
    private final double getDoubleX() {
        return this.doubleX;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of doubleX property to assign.
     */
    private final void setDoubleX(double value) {
        try {
            this.doubleX = (double) value;
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * Return the byteX property.
     *
     * @return A value of byteX property.
     */
    @Override
    public final byte byteX() {
        return this.byteX;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of byteX property.
     */
    @SuppressWarnings("unused")
    private final byte getByteX() {
        return this.byteX;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of byteX property to assign.
     */
    private final void setByteX(byte value) {
        try {
            this.byteX = (byte) value;
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * Return the shortX property.
     *
     * @return A value of shortX property.
     */
    @Override
    public final short shortX() {
        return this.shortX;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of shortX property.
     */
    @SuppressWarnings("unused")
    private final short getShortX() {
        return this.shortX;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of shortX property to assign.
     */
    private final void setShortX(short value) {
        try {
            this.shortX = (short) value;
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * Return the charX property.
     *
     * @return A value of charX property.
     */
    @Override
    public final char charX() {
        return this.charX;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of charX property.
     */
    @SuppressWarnings("unused")
    private final char getCharX() {
        return this.charX;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of charX property to assign.
     */
    private final void setCharX(char value) {
        try {
            this.charX = (char) value;
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * Return the booleanX property.
     *
     * @return A value of booleanX property.
     */
    @Override
    public final boolean booleanX() {
        return this.booleanX;
    }

    /**
     * Provide classic getter API.
     *
     * @return A value of booleanX property.
     */
    @SuppressWarnings("unused")
    private final boolean getBooleanX() {
        return this.booleanX;
    }

    /**
     * Provide classic setter API.
     *
     * @paran value A new value of booleanX property to assign.
     */
    private final void setBooleanX(boolean value) {
        try {
            this.booleanX = (boolean) value;
        } catch (UnsupportedOperationException e) {
        } catch (Throwable e) {
            throw quiet(e);
        }
    }

    /**
     * Show all property values.
     *
     * @return All property values.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Primitive [");
        builder.append("intX=").append(intX).append(", ");
        builder.append("longX=").append(longX).append(", ");
        builder.append("floatX=").append(floatX).append(", ");
        builder.append("doubleX=").append(doubleX).append(", ");
        builder.append("byteX=").append(byteX).append(", ");
        builder.append("shortX=").append(shortX).append(", ");
        builder.append("charX=").append(charX).append(", ");
        builder.append("booleanX=").append(booleanX).append("]");
        return builder.toString();
    }

    /**
     * Generates a hash code for a sequence of property values. The hash code is generated as if all the property values were placed into an array, and that array were hashed by calling Arrays.hashCode(Object[]). 
     *
     * @return A hash value of the sequence of property values.
     */
    @Override
    public int hashCode() {
        return Objects.hash(intX, longX, floatX, doubleX, byteX, shortX, charX, booleanX);
    }

    /**
     * Returns true if the all properties are equal to each other and false otherwise. Consequently, if both properties are null, true is returned and if exactly one property is null, false is returned. Otherwise, equality is determined by using the equals method of the base model. 
     *
     * @return true if the all properties are equal to each other and false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Primitive == false) {
            return false;
        }

        Primitive other = (Primitive) o;
        if (intX != other.intX) return false;
        if (longX != other.longX) return false;
        if (floatX != other.floatX) return false;
        if (doubleX != other.doubleX) return false;
        if (byteX != other.byteX) return false;
        if (shortX != other.shortX) return false;
        if (charX != other.charX) return false;
        if (booleanX != other.booleanX) return false;
        return true;
    }

    /** The singleton builder. */
    public static final  Ìnstantiator<?> with = new Ìnstantiator();

    /**
     * Namespace for {@link Primitive}  builder methods.
     */
    public static class Ìnstantiator<Self extends Primitive & ÅssignableÅrbitrary<Self>> {

        /**
         * Create new {@link Primitive} with the specified intX property.
         * 
         * @return The next assignable model.
         */
        public ÅssignableLongX<ÅssignableFloatX<ÅssignableDoubleX<ÅssignableByteX<ÅssignableShortX<ÅssignableCharX<ÅssignableBooleanX<Self>>>>>>> intX(int intX) {
            Åssignable o = new Åssignable();
            o.intX(intX);
            return o;
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableIntX<Next> {

        /**
         * Assign intX property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next intX(int value) {
            ((Primitive) this).setIntX(value);
            return (Next) this;
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableLongX<Next> {

        /**
         * Assign longX property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next longX(long value) {
            ((Primitive) this).setLongX(value);
            return (Next) this;
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableFloatX<Next> {

        /**
         * Assign floatX property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next floatX(float value) {
            ((Primitive) this).setFloatX(value);
            return (Next) this;
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableDoubleX<Next> {

        /**
         * Assign doubleX property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next doubleX(double value) {
            ((Primitive) this).setDoubleX(value);
            return (Next) this;
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableByteX<Next> {

        /**
         * Assign byteX property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next byteX(byte value) {
            ((Primitive) this).setByteX(value);
            return (Next) this;
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableShortX<Next> {

        /**
         * Assign shortX property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next shortX(short value) {
            ((Primitive) this).setShortX(value);
            return (Next) this;
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableCharX<Next> {

        /**
         * Assign charX property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next charX(char value) {
            ((Primitive) this).setCharX(value);
            return (Next) this;
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableBooleanX<Next> {

        /**
         * Assign booleanX property.
         * 
         * @param value A new value to assign.
         * @return The next assignable model.
         */
        default Next booleanX(boolean value) {
            ((Primitive) this).setBooleanX(value);
            return (Next) this;
        }
    }

    /**
     * Property assignment API.
     */
    public static interface ÅssignableÅrbitrary<Next extends Primitive> {
    }

    /**
     * Internal aggregated API.
     */
    protected static interface ÅssignableAll extends ÅssignableIntX, ÅssignableLongX, ÅssignableFloatX, ÅssignableDoubleX, ÅssignableByteX, ÅssignableShortX, ÅssignableCharX, ÅssignableBooleanX {
    }

    /**
     * Mutable Model.
     */
    private static final class Åssignable extends Primitive implements ÅssignableAll, ÅssignableÅrbitrary {
    }

    /**
     * The identifier for properties.
     */
    static final class My {
        static final String IntX = "intX";
        static final String LongX = "longX";
        static final String FloatX = "floatX";
        static final String DoubleX = "doubleX";
        static final String ByteX = "byteX";
        static final String ShortX = "shortX";
        static final String CharX = "charX";
        static final String BooleanX = "booleanX";
    }
}
