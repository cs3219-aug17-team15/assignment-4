package main;

import static spark.Spark.get;
import static spark.Spark.path;

import controllers.ViewController;

public class Router {
  private static final String ROUTER_INIT = "Starting up router...";
  private static final String ROUTER_STARTED = "Router initialised and ready to serve.";

  public void initAndServe() {
    System.out.println(ROUTER_INIT);

    // setup endpoints
    path("/tasks", () -> {
      get("/1", ViewController.task1);
      get("/2", ViewController.task2);
      get("/3", ViewController.task3);
      get("/4", ViewController.task4);
      get("/5", ViewController.task5);
    });

    System.out.println(ROUTER_STARTED);
  }

}
