package io.ib67.liquid;

import io.ib67.liquid.tools.Liquid;
import io.ib67.liquid.tools.Shadow;
import sun.misc.Unsafe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class LiquidFactory {
    public static <T extends AbstractInjector> T produce(T obj,Object target){
        boolean shadow;
        if(obj.getClass().isAnnotationPresent(Liquid.class)){
            Liquid liquid = obj.getClass().getAnnotation(Liquid.class);
            shadow = liquid.shadowAll();
        }
        Map<Shadow, Pair<Long,Long>> cache = new HashMap<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            for (Annotation annotation : field.getAnnotations()) {
               if(annotation instanceof Shadow){
                    Shadow op = (Shadow) annotation;
                    if(op.skip()){
                       break;
                    }
                    try {
                        String targetedFieldName = op.mapTo().isEmpty()?field.getName():op.mapTo();
                        Pair<Long,Long> result = new Pair(0L,0L);
                        Unsafe usf =AbstractInjector.getUnsafe();
                        result.key = usf.objectFieldOffset(target.getClass().getDeclaredField(targetedFieldName)); //target field offset
                        result.value = usf.objectFieldOffset(obj.getClass().getDeclaredField(field.getName()));
                        // done.
                        cache.put(op,result);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
               }
            }
        }
        //cache prepared
        obj.configure(target,cache);
        obj.syncFrom();
        return obj;
    }
}
