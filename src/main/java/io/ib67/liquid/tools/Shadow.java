package io.ib67.liquid.tools;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Map a field.
 * Parameter is a toggle for {@see Liquid} which skips `allShadow` property.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Shadow {

    boolean value() default false;
}
