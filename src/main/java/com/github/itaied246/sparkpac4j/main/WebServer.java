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

	final static String clientId = "788339d7-1c44-4732-97c9-134cb201f01f";
	static final String secret = "we/31zi+JYa7zOugO4TbSw0hzn+hv2wmENO9AS3T84s=";
	static final String discoveryUri = "https://login.microsoftonline.com/38c46e5a-21f0-46e5-940d-3ca06fd1a330/.well-known/openid-configuration";
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
