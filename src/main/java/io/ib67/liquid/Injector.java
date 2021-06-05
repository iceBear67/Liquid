package io.ib67.liquid;

import java.lang.invoke.MethodType;

public interface Injector {
    <R> R method(String name, Class<R> returnType, MethodType paramType,Object... params);
    <R> R field(String name,Class<R> returnType) throws NoSuchFieldException;
    <R> R staticMethod(String name, Class<R> returnType, MethodType paramType,Object... params);
    <R> R staticField(String name,Class<R> returnType);
    //todo hasField / Method [static]
}
