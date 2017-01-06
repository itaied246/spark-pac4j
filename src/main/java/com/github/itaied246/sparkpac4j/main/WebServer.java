package com.github.itaied246.sparkpac4j.main;

import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import org.pac4j.core.config.Config;
import org.pac4j.sparkjava.CallbackRoute;
import org.pac4j.sparkjava.SecurityFilter;

import com.github.itaied246.sparkpac4j.auth.OidcConfigFactory;
import com.github.itaied246.sparkpac4j.controllers.MainController;

public class WebServer {

	final static String clientId = "8819981768.apps.googleusercontent.com";
	static final String secret = "secret";
	static final String discoveryUri = "https://oauthssodemo.appspot.com/oauthcallback";
	static final String callbackUrl = "http://localhost:8080/callback";

	public static void main(String[] args) {

		OidcConfigFactory oidcConfigFactory = new OidcConfigFactory(clientId, secret,
				discoveryUri, callbackUrl);
		Config authConfig = oidcConfigFactory.build();
		MainController mainController = new MainController();

		configureServer();

		setAuthCallbackRoutes(authConfig);

		protectRoutes(authConfig);

		setWebServerRoutes(mainController);
	}

	private static void configureServer() {
		port(8080);
		exception(Exception.class, (exception, request, response) -> {
			exception.printStackTrace();
		});
	}

	private static void setAuthCallbackRoutes(Config authConfig) {
		CallbackRoute callbackRoute = new CallbackRoute(authConfig);
		get("/callback", callbackRoute);
		post("/callback", callbackRoute);
	}

	private static void protectRoutes(Config authConfig) {
		before("/auth", new SecurityFilter(authConfig, "OidcClient"));
	}

	private static void setWebServerRoutes(MainController mainController) {
		get("/", mainController::hello);
		get("/auth", mainController::authHello);
	}

}
