package main;

import static spark.Spark.*;

import controllers.LogicController;
import controllers.ViewController;

public class Router {
  private static final String ROUTER_INIT = "Starting up router...";
  private static final String ROUTER_STARTED = "Router initialised and ready to serve.";

  public void initAndServe() {
    System.out.println(ROUTER_INIT);
    // for local dev
    String projectDir = System.getProperty("user.dir");
    String staticDir = "/src/main/resources/velocity";
    staticFiles.externalLocation(projectDir + staticDir);

    // for deployment
    // staticFiles.location("/velocity");

    LogicController.getInstance();

    // setup endpoints
    path("/tasks", () -> {
      get("/1", ViewController.task1);
      get("/2", ViewController.task2);
      get("/3", ViewController.task3);
      get("/4", ViewController.task4);
      get("/5", ViewController.task5);
    });

    path("/api", () -> {
      get("/task1", LogicController.task1JSON);
      get("/task2", LogicController.task2JSON);
      get("/task3", LogicController.task3JSON);
      get("/task5", LogicController.task5JSON);
    });

    System.out.println(ROUTER_STARTED);
  }

}
