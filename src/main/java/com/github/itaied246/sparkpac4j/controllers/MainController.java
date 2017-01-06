package com.github.itaied246.sparkpac4j.controllers;

import java.util.Optional;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.sparkjava.SparkWebContext;

import spark.Request;
import spark.Response;

public class MainController {

	public String hello(Request req, Response res) {
		return "hello world!";
	}

	public String authHello(Request req, Response res) {
		String clientName = getClientName(req, res);
		return "Hello " + clientName;
	}

	private String getClientName(Request req, Response res) {
		WebContext context = new SparkWebContext(req, res);
		ProfileManager<CommonProfile> manager = new ProfileManager<CommonProfile>(
				context);
		Optional<CommonProfile> profile = manager.get(true);
		return profile.map(commonProfile -> commonProfile.getClientName())
				.orElse("Unknown");
	}

}
