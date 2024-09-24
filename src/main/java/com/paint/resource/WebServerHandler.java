package com.paint.resource;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;

public class WebServerHandler {

	// Web
	private final InetAddress DEVICE_IP_ADDR = InetAddress.getLocalHost();
	private final String LOCAL_HOST_IP_ADDR = "localhost";
	private final InetSocketAddress WEB_ADDRESS = new InetSocketAddress(DEVICE_IP_ADDR,8080);
	private final String INITIAL_PROJECT_PATH = System.getProperty("user.home") + "/.paint/projects";
	private HttpServer server;
	private FileHandler fileHandler;

	public WebServerHandler() throws UnknownHostException {
	}

	public void createNewServer() throws IOException {
		System.out.println(InetAddress.getLocalHost());
		this.server = HttpServer.create(WEB_ADDRESS, 0);
		fileHandler = new FileHandler();
		fileHandler.setCurrentFile(null);
		this.server.createContext("/", fileHandler);

		this.server.setExecutor(null);
	}

	public void updateCurrentFile(File file) {
		if (fileHandler == null) {
			return;
		}

		fileHandler.setCurrentFile(file);
	}

	public void startHttpServer() {
        this.server.start();
	}

	/**
	 * Stops the httpServer after a 2-second delay
	 */
	public void stopHttpServer() {
		this.server.stop(2);
	}
}

class FileHandler implements HttpHandler {
	private File currentFile;

	public void setCurrentFile(File currentFile) {
		this.currentFile = currentFile;
	}

	/**
	 * @param exchange the exchange containing the request from the
	 *                 client and used to send the response
	 * @throws IOException
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {

		if (currentFile == null) {
			String res = "User has no projects currently selected and/or saved!";
			exchange.sendResponseHeaders(404, res.length());
			OutputStream outputStream = exchange.getResponseBody();
			outputStream.write(res.getBytes());
			outputStream.close();
			return;
		}

		byte[] imageBytes = Files.readAllBytes(currentFile.toPath());

		// Set the appropriate content type (MIME type)
		String contentType = Files.probeContentType(currentFile.toPath());
		exchange.getResponseHeaders().set("Content-Type", contentType);

		// Send the file as a response
		exchange.sendResponseHeaders(200, imageBytes.length);
		OutputStream os = exchange.getResponseBody();
		os.write(imageBytes);
		os.close();

	}
}
