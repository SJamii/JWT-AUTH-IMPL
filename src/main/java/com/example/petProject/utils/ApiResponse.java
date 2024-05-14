package com.example.petProject.utils;

import com.example.petProject.enums.ResponseType;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ApiResponse {
    private final ResponseType type;
    private int status = HttpStatus.OK.value(); // Default to HTTP 200 OK
    private Object data = null;
    private Object meta = null;
    private String message = null;
    private Object errors = null;

    public ApiResponse(ResponseType type) {
        this.type = type;
    }

    public static ApiResponse success(Object data, int status,  String message) {
        ApiResponse response = new ApiResponse(ResponseType.DATA);
        response.data = data;
        response.message = message;
        response.status = status;
        return response;
    }

    public static ApiResponse success(Object data, int status) {
        ApiResponse response = new ApiResponse(ResponseType.DATA);
        response.data = data;
        response.status = status;
        return response;
    }

    public JSONObject getJson() {
        Map<String, Object> maps = new HashMap<>();

        switch (this.type) {
            case DATA:
                maps.put("status", status);
                maps.put("data", data);
                maps.put("meta", meta);
                maps.put("message", message);
            case ERROR:
                maps.put("status", status);
                maps.put("message", message);
                maps.put("errors", errors);
        }

        return new JSONObject(maps);
    }

    public static ApiResponse error(Object errors, int status, String message) {
        ApiResponse response = new ApiResponse(ResponseType.ERROR);
        response.errors = errors;
        response.message = message;
        response.status = status;
        return response;
    }
}
