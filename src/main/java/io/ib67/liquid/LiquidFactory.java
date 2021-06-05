package io.ib67.liquid;

import io.ib67.liquid.tools.Access;
import io.ib67.liquid.tools.Shadow;
import io.ib67.liquid.tools.Static;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class LiquidFactory {
    public static <T> T produce(T obj,Object target){
        T o = obj;
        for (Field field : o.getClass().getFields()) {
            for (Annotation annotation : field.getAnnotations()) {
                if(annotation instanceof Access){
                    try {
                        target.getClass().getField(field.getName()).setAccessible(true);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
                if(annotation instanceof Shadow){
                    try {
                        field.set(o,target.getClass().getField(field.getName()).get(field.isAnnotationPresent(Static.class)?null:target));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return o;
    }
}
