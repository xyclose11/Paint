package com.paint.handler;

import com.paint.controller.InfoController;
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
	private InfoController infoController;

	private final String CHECKMARK_ICON = "fltfal-checkmark-16";
	private final String DISMISS_ICON = "fltfal-dismiss-16";


	public WebServerHandler() throws UnknownHostException {
	}


	/**
	 * Creates an instance of HttpServer with the Web Address (IP) being automatically
	 * set to the devices IP and Localhost both on port 8080. All content for HTTP requests
	 * are set to the '/' directory.
	 *
	 * To navigate to the server, go to a web browser and type "localhost:8080" OR
	 * "127.0.0.1:8080" OR "{DEVICE_IP}:8080".
	 *
	 * @throws IOException
	 * */
	public void createNewServer() throws IOException {
		System.out.println(InetAddress.getLocalHost());
		this.server = HttpServer.create(WEB_ADDRESS, 0);
		fileHandler = new FileHandler();
		fileHandler.setCurrentFile(null);
		this.server.createContext("/", fileHandler);

		this.server.setExecutor(null);
	}

	/**
	 * Updates the current file that is being served by the HttpServer instance.
	 *
	 * NOTE: Will automatically return if the instance of FileHandler is null.
	 * @param file a {File} object
	 * */
	public void updateCurrentFile(File file) {
		if (file == null) {
			this.infoController.getWebserverStatusIcon().setIconLiteral(DISMISS_ICON);
			this.infoController.getWebserverStatusLink().setText("Web service offline");
		} else {
			// Update status icon
			this.infoController.getWebserverStatusIcon().setIconLiteral(CHECKMARK_ICON);
			// update link
			this.infoController.getWebserverStatusLink().setText("http://" + DEVICE_IP_ADDR.getHostAddress() + ":8080");
		}

		if (fileHandler == null) {
			return;
		}

		fileHandler.setCurrentFile(file);
	}

	/**
	 * Starts the HttpServer
	 * */
	public void startHttpServer() {
        this.server.start();
	}

	/**
	 * Stops the HttpServer after a 2-second delay
	 */
	public void stopHttpServer() {
		this.server.stop(0);
		if (this.infoController != null) {
			// Update status icon
			this.infoController.getWebserverStatusIcon().setIconLiteral(DISMISS_ICON);
			// update link
			this.infoController.getWebserverStatusLink().setText("Web service is offline.");
		}
	}

	public InfoController getInfoController() {
		return infoController;
	}

	public void setInfoController(InfoController infoController) {
		this.infoController = infoController;
	}
}

/**
 * Implements {HttpHandler} to host Files, to be used by an instance of HttpServer
 * context.
 *
 *
 * */
class FileHandler implements HttpHandler {
	private File currentFile;

	public void setCurrentFile(File currentFile) {
		this.currentFile = currentFile;
	}

	/**
	 * Checks to see if there is a file that has been selected by the user.
	 *
	 *
	 *     NOTE: Will return early if there is no file set
	 *     Will return page with error 404.
	 * 
	 *
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
