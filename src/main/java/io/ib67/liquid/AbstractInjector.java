package io.ib67.liquid;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

// Todo: Bypass protection & better performance
public abstract class AbstractInjector implements Injector{
    private Object target;
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
            return (R) getTargetClass().getField(name).get(target);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
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
            return (R) getTargetClass().getField(name).get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Class<?> getTargetClass(){
        return target.getClass();
    }

    /**
     * Internal use only
     * @param target
     */
    @Deprecated
    protected void setTarget(Object target){
     this.target=target;
    }

    protected void sync(){
        //todo
    }
}
