package controllers;

import main.Router;
import spark.Request;
import spark.Response;
import spark.Route;

public class AuthorController {
  public static Route test = (Request req, Response resp) -> {
    return Router.paperStore.getAuthors();
  };
}
