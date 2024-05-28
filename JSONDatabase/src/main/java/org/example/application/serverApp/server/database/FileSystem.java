package org.example.application.serverApp.server.database;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.example.application.serverApp.server.util.Directory;
import org.example.application.serverApp.server.util.Response;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileSystem implements Datasource {

    private final static String DB_FILE = "db.json";
    private final static String RESPONSE_SUCCESS = "OK";
    private final static String RESPONSE_ERROR = "ERROR";
    private final static String INVALID_KEY = "No such key";
    private final static String SET = "SET";
    private final static String GET = "GET";
    private final static String DELETE = "DELETE";

    private final Directory serverDirectory;

    private JsonObject database;
    private String databaseFilePath;
    private Lock readLock;
    private Lock writeLock;

    public FileSystem(Directory directory) {
        this.serverDirectory = directory;
        this.database = new JsonObject();
        loadFileSystem();
    }

    private void loadFileSystem() {
        loadSaveFile();
        loadDatabase();
        loadLocks();
    }

    private void loadSaveFile() {
        this.serverDirectory.createFile(DB_FILE);
        this.databaseFilePath = this.serverDirectory.getFullPath();
    }

    private void loadDatabase() {
        try {

            String fileContent = this.serverDirectory.readFile(this.databaseFilePath);
            this.database = new Gson().fromJson(fileContent, JsonObject.class);

        } catch (IOException e) {
            throw new RuntimeException("Error reading database from file \"" + DB_FILE + "\": " + e.getMessage());
        }
    }

    private void loadLocks() {
        final ReadWriteLock lock = new ReentrantReadWriteLock();
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
    }

    @Override
    public Response set(JsonElement key, JsonElement value) {
        try {

            this.writeLock.lock();

            if (!key.isJsonNull()) {

                if (key.isJsonPrimitive()) {

                    String jsonKey = key.getAsString();
                    this.database.add(jsonKey, value);

                } else if (key.isJsonArray()) {

                    JsonArray array = key.getAsJsonArray();
                    String lastKey = array.remove(array.size() - 1).getAsString();
                    handleJsonElement(array, SET).getAsJsonObject().add(lastKey, value);
                }
                return Response.newBuilder().response(RESPONSE_SUCCESS).build();
            }
            return Response.newBuilder().response(RESPONSE_ERROR).reason(INVALID_KEY).build();

        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public Response get(JsonElement key) {
        boolean valueFound = false;
        try {

            this.readLock.lock();

            JsonElement value = null;

            if (!key.isJsonNull()) {

                if (key.isJsonPrimitive()) {

                    String keyString = key.getAsString();

                    if (this.database.has(keyString)) {
                        value = this.database.get(keyString);
                        valueFound = true;
                    }

                } else if (key.isJsonArray()) {

                    JsonArray array = key.getAsJsonArray();
                    String lastKey = array.remove(array.size() - 1).getAsString();
                    JsonElement temp = handleJsonElement(array, GET);

                    if (temp != null) {
                        value = temp.getAsJsonObject().get(lastKey);
                        valueFound = true;
                    }
                }
            }
            return (valueFound) ? Response.newBuilder().response(RESPONSE_SUCCESS).value(value).build() :
                    Response.newBuilder().response(RESPONSE_ERROR).reason(INVALID_KEY).build();

        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public Response delete(JsonElement key) {
        boolean valueFound = false;
        try {

            this.writeLock.lock();

            if (!key.isJsonNull()) {

                if (key.isJsonPrimitive()) {

                    String keyString = key.getAsString();

                    if (this.database.has(keyString)) {

                        this.database.remove(keyString);
                        valueFound = true;
                    }

                } else if (key.isJsonArray()) {

                    JsonArray array = key.getAsJsonArray();
                    String lastKey = array.remove(array.size() - 1).getAsString();
                    JsonElement temp = handleJsonElement(array, DELETE);

                    if (temp != null) {
                        temp.getAsJsonObject().remove(lastKey);
                        valueFound = true;
                    }
                }
            }
            return (valueFound) ? Response.newBuilder().response(RESPONSE_SUCCESS).build() :
                    Response.newBuilder().response(RESPONSE_ERROR).reason(INVALID_KEY).build();

        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public Response exit() {
        try {

            this.readLock.lock();

            try {

                String jsonDatabase = toJson();
                this.serverDirectory.writeToFile(this.databaseFilePath, jsonDatabase);
                return Response.newBuilder().response(RESPONSE_SUCCESS).build();

            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }
            return Response.newBuilder()
                    .response(RESPONSE_ERROR)
                    .build();

        } finally {
            this.readLock.unlock();
        }
    }

    private String toJson() {
        return new Gson().newBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(this.database);
    }

    private JsonElement handleJsonElement(JsonArray array, String action) {
        JsonObject temp = this.database;

        for (JsonElement key : array) {
            String keyString = key.getAsString();

            if (!temp.has(keyString) || !temp.get(keyString).isJsonObject()) {
                if (action.equals(SET)) {
                    temp.add(keyString, new JsonObject());
                } else {
                    return null;
                }
            }
            temp = temp.getAsJsonObject(keyString);
        }
        return temp;
    }
}
