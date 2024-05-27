package org.example.application.serverApp.server.request;

import org.example.application.serverApp.server.util.Response;

public interface Command {

    void execute();

    Response getResponse();
}
