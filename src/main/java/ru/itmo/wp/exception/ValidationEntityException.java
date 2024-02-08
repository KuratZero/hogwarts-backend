package ru.itmo.wp.exception;

public class ValidationEntityException extends RuntimeException {
    public ValidationEntityException(String entity, String what) {
        super(entity + " " + what);
    }
}
