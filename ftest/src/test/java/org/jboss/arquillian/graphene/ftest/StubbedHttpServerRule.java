package org.jboss.arquillian.graphene.ftest;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;
import org.junit.rules.ExternalResource;

public class StubbedHttpServerRule extends ExternalResource {

    private final int port;

    private HttpServer server;

    public StubbedHttpServerRule(int port) {
        this.port = port;
    }

    @Override
    protected void before() throws Throwable {
        final byte[] response = "Hello Drone!".getBytes();
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", httpExchange -> {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
            httpExchange.getResponseBody().write(response);
            httpExchange.close();
        });
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    @Override
    protected void after() {
        server.stop(2);
    }

    public int getPort() {
        return port;
    }
}
