package com.kramarenko.validation;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.Collections.emptyList;

public abstract class Result<T> {
    private static final String ERROR_DELIMITER = ", ";

    private final T value;
    private final List<String> errors;

    public abstract boolean isSuccess();

    public boolean isFailure() {
        return !isSuccess();
    }

    public static <T> Result<T> failure(String... errors) {
        return new FailureResult<>(ImmutableList.copyOf(errors));
    }

    public static <T> Result<T> failure(Collection<String> errors) {
        return new FailureResult<>(errors);
    }

    public static <T> Result<T> success(T value) {
        return new SuccessfulResult<>(value);
    }

    public static <T> Result<T> success() {
        return new SuccessfulResult<>();
    }

    private Result(Collection<String> errors) {
        this.value = null;
        this.errors = ImmutableList.copyOf(errors);
    }

    private Result(T value) {
        this.value = value;
        this.errors = emptyList();
    }

    public T get() {
        if (isSuccess()) {
            return value;
        }
        throw new NoSuchElementException("No value present");
    }

    public boolean isPresent() {
        return isSuccess() && value != null;
    }

    public List<String> getErrors() {
        return ImmutableList.copyOf(errors);
    }

    public String getErrorString() {
        return String.join(ERROR_DELIMITER, errors);
    }

    public static class SuccessfulResult<T> extends Result<T> {

        @Override
        public boolean isSuccess() {
            return true;
        }

        private SuccessfulResult(T value) {
            super(value);
        }

        private SuccessfulResult() {
            super((T) null);
        }
    }

    public static class FailureResult<T> extends Result<T> {

        @Override
        public boolean isSuccess() {
            return false;
        }

        private FailureResult(Collection<String> errors) {
            super(errors);
        }
    }
}
