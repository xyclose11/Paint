package com.paint.resource;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;

public class WebServerHandler {

	// Web
	private final InetAddress DEVICE_IP_ADDR = InetAddress.getLocalHost();
	private final String LOCAL_HOST_IP_ADDR = "localhost";
	private final InetSocketAddress WEB_ADDRESS = new InetSocketAddress(DEVICE_IP_ADDR,8080);
	private final String INITIAL_PROJECT_PATH = System.getProperty("user.home") + "/.paint/projects";
	private HttpServer server;

	public WebServerHandler() throws UnknownHostException {
	}

	public void createNewServer() throws UnknownHostException {
		System.out.println(InetAddress.getLocalHost());
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
