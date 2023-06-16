package com.example.simplebroker.exception;

import java.util.UUID;

public final class ExceptionFactory {

    @SuppressWarnings("rawtypes")
    public static NotFoundException getNotFoundException(Class notFoundObject) {
        return new NotFoundException(notFoundObject.getName() + " not found");
    }

    @SuppressWarnings("rawtypes")
    public static NotFoundException getNotFoundException(Class notFoundObject, String name) {
        return new NotFoundException(notFoundObject.getName() + " not found");
    }

    @SuppressWarnings("rawtypes")
    public static NotFoundException getNotFoundException(Class notFoundObject, UUID id) {
        return new NotFoundException(notFoundObject.getName() + " not found");
    }
}
