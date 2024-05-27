package org.example.application.serverApp.server.request;

import com.google.gson.JsonElement;
import org.example.application.serverApp.server.database.Datasource;
import org.example.application.serverApp.server.util.Response;

public class DeleteRequest implements Command {

    private final JsonElement key;
    private Response response;
    private final Datasource datasource;

    public DeleteRequest(JsonElement key, Datasource source) {
        this.key = key;
        this.datasource = source;
    }

    @Override
    public void execute() {
        this.response = this.datasource.delete(this.key);
    }

    @Override
    public Response getResponse() {
        return this.response;
    }
}
