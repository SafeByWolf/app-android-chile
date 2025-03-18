package com.safebywolf.safebywolf.Model;

public class Response {
    int success;
    int failure;

    public Response() {
    }

    public Response(int success, int failure) {
        this.success = success;
        this.failure = failure;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

}
