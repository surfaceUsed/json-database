package org.example.application.serverApp.server.util;

import java.io.IOException;

public abstract class Directory {

    public abstract void createFile(String fileName);

    public abstract String readFile(String path) throws IOException;

    public abstract String getPath();

    public abstract void writeToFile(String path, String data) throws IOException;

    public static Directory loadDirectory(String directoryPath) {

        DirectoryLoader loader = new DirectoryLoader(directoryPath);

        if (loader.isDirectoryValid()) {

            System.out.println("Directory valid!");
            return loader;
        }

        System.out.println("Directory invalid");
        throw new RuntimeException();
    }
}