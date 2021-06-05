package io.ib67.liquid.tools;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Liquid {
    boolean accessAll();
    boolean shadowAll();
    //todo properties maybe?
}
