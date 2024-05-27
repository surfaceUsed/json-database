package org.example.application.serverApp.server.request;

import org.example.application.serverApp.server.util.Response;

public class ExecuteRequest {

    private final Command command;

    public ExecuteRequest(Command command) {
        this.command = command;
    }

    public void execute() {
        this.command.execute();
    }

    public Response getResponse() {
        return this.command.getResponse();
    }
}
