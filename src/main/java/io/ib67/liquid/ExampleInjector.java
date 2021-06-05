package io.ib67.liquid;

import io.ib67.liquid.tools.Access;
import io.ib67.liquid.tools.Liquid;
import io.ib67.liquid.tools.Shadow;

import java.lang.invoke.MethodType;

@Liquid(
        accessAll = false,
        shadowAll = false
)
// target class: java.lang.String
public class ExampleInjector extends AbstractInjector{
    @Shadow
    @Access
    private char[] value; // private final char[] value; in String.class
    public void tryModify(){
        value = null; //example 1 (modify)
        System.out.println(value.length); // eq
        System.out.println(field("value", char[].class).length);

        method("charAt",char.class, MethodType.methodType(int.class),114514);
        long serialVersionUID = staticField("serialVersionUID",long.class);
    }
}
