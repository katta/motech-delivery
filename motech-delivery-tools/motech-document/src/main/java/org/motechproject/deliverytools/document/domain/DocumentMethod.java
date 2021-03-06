package org.motechproject.deliverytools.document.domain;

import java.lang.reflect.Method;

public class DocumentMethod {
    private Object bean;
    private Method method;

    public DocumentMethod(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

    public String getClassName() {
        return method.getDeclaringClass().getName();
    }

    public Class<?>[] arguments() {
        return method.getParameterTypes();
    }

    public Class<?> returnType() {
        return method.getReturnType();
    }

    public boolean returns(){
        return method.getGenericReturnType() != null;
    }

    public String name() {
        return method.getName();
    }

    public boolean hasArguments() {
        return method.getParameterTypes().length > 0;
    }

}
