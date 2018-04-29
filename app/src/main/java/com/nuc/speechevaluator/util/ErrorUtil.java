package com.nuc.speechevaluator.util;

public class ErrorUtil {

    public static void invokeThrowable(Closure<Throwable> closure, Throwable throwable) {
        if (closure != null) {
            closure.invoke(throwable);
        }
    }

}
