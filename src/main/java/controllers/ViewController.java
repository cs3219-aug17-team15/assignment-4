package controllers;

import spark.Request;
import spark.Response;
import spark.Route;

public class ViewController {
  public static Route task1 = (Request req, Response resp) -> {
    return "Hello World";
  };

  public static Route task2 = (Request req, Response resp) -> {
    return "Hello World";
  };

  public static Route task3 = (Request req, Response resp) -> {
    return "Hello World";
  };

  public static Route task4 = (Request req, Response resp) -> {
    return "Hello World";
  };

  public static Route task5 = (Request req, Response resp) -> {
    return "Hello World";
  };
}
