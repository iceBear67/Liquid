package io.ib67.liquid;

import io.ib67.liquid.tools.Shadow;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.Map;

// Todo: Bypass protection & better performance
public abstract class AbstractInjector implements Injector{
    private Object target;
    private Map<Shadow, Pair<Long,Long>> cache;
    private static Unsafe unsafe;
    @Override
    public <R> R method(String name, Class<R> returnType, MethodType paramsType,Object... args) {
        try {
            return (R) MethodHandles.lookup().findVirtual(getTargetClass(),name,paramsType).invoke(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    public <R> R field(String name, Class<R> returnType){
        try {
            Field f = getTargetClass().getField(name);
            return (R) getUnsafe().getObject(target,getUnsafe().objectFieldOffset(f));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <R> R staticMethod(String name, Class<R> returnType, MethodType params,Object... args) {
        try {
            return (R) MethodHandles.lookup().findStatic(getTargetClass(),name,params).invoke(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    @Override
    public <R> R staticField(String name, Class<R> returnType) {
        try {
            Field f = getTargetClass().getField(name);
            return (R) getUnsafe().getObject(null,getUnsafe().objectFieldOffset(f));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Class<?> getTargetClass(){
        return target.getClass();
    }
    static Unsafe getUnsafe(){
        if(unsafe==null){
            try {
                Field field = Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                unsafe = (Unsafe) field.get(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return unsafe;
    }
    /**
     * Internal use only
     * @param target
     */
    void configure(Object target, Map<Shadow, Pair<Long,Long>> cache)
    {
     this.target=target;
     this.cache=cache;
    }

    /**
     * Sync from injected target
     */
    protected void syncFrom(){
        for (Map.Entry<Shadow, Pair<Long,Long>> entry : cache.entrySet()) {
            if(!entry.getKey().skip()){
                Pair<Long,Long> targets=entry.getValue();
                Object obj = entry.getKey().targetStatically()?null:target;
                Object inject = entry.getKey().injectorStatically()? null  : this;
                getUnsafe().putObject(inject,targets.value,getUnsafe().getObject(obj,targets.key));
            }
        }
    }
    protected void syncTo(){
        for (Map.Entry<Shadow, Pair<Long,Long>> entry : cache.entrySet()) {
            if(!entry.getKey().skip()){
                Pair<Long,Long> targets=entry.getValue();
                Object obj = entry.getKey().targetStatically()?null:target;
                Object inject = entry.getKey().injectorStatically()? null  : this;
                getUnsafe().putObject(obj,targets.key,getUnsafe().getObject(inject,targets.value));
            }
        }
    }
}
