package org.example.application.serverApp.server.database;

import com.google.gson.JsonElement;
import org.example.application.serverApp.server.util.Response;

public interface Datasource {

    Response set(JsonElement key, JsonElement value);

    Response get(JsonElement key);

    Response delete(JsonElement key);

    Response exit();
}
