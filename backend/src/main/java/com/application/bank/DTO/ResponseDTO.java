package com.application.bank.DTO;

public class ResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;

    public ResponseDTO(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseDTO<T> success(String message, T data) {
        return new ResponseDTO<>(true, message, data);
    }

    public static <T> ResponseDTO<T> error(String message) {
        return new ResponseDTO<>(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
