package com.paint.resource;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;

import java.net.InetSocketAddress;
import java.nio.file.Path;

public class WebServerHandler {

	// Web
	private final InetSocketAddress WEB_ADDRESS = new InetSocketAddress("localhost",8080);
	private final String INITIAL_PROJECT_PATH = "C:/Users/harri/.paint/projects";
	private HttpServer server;

	public void createNewServer() {
		this.server = SimpleFileServer.createFileServer(WEB_ADDRESS, Path.of(INITIAL_PROJECT_PATH), SimpleFileServer.OutputLevel.INFO);
	}

	public void startHttpServer() {
        this.server.start();
	}

	/**
	 * Stops the httpServer after a 5-second delay
	 */
	public void stopHttpServer() {
		this.server.stop(5);
	}
}
