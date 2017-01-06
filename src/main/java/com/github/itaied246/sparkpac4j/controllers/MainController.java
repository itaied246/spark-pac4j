package com.github.itaied246.sparkpac4j.controllers;

import spark.Request;
import spark.Response;

public class MainController {

	public String hello(Request req, Response res) {
		return "hello world!";
	}

	public String authHello(Request req, Response res) {
		return "authenticated hello world!";
	}

}
