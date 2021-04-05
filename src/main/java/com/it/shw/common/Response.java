package com.it.shw.common;

import lombok.Data;

@Data
public class Response {

    private String code;

    private String message;

    private Object data;

    public Response(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Response error(String message) {
        return new Response("error", message);
    }

    public static Response ok() {
        return new Response("ok", null);
    }

    public Response data(Object data) {
        this.data = data;
        return this;
    }
}