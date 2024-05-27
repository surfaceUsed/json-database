package org.example.application.serverApp.server.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public final class Response {

    private String response;
    private String reason;
    private JsonElement value;

    private Response() {}

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public JsonElement getValue() {
        return value;
    }

    public void setValue(JsonElement value) {
        this.value = value;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    // Inner builder class.
    public static class Builder {

        private final Response response;

        private Builder() {
            this.response = new Response();
        }

        public Builder response(String response) {
            this.response.setResponse(response);
            return this;
        }

        public Builder reason(String reason) {
            this.response.setReason(reason);
            return this;
        }

        public Builder value(JsonElement value) {
            this.response.setValue(value);
            return this;
        }

        public Response build() {
            return this.response;
        }
    }
}