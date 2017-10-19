package main;

import static spark.Spark.*;
import java.nio.file.Paths;

import controllers.AuthorController;
import parser.Parser;
import storage.PaperStore;

public class Router {
  // replace resource.json with the file name of the dataset
  private static final String RES_PATH = Paths.get(System.getProperty("user.dir"), "assets/resource.json").toString();
  private static final String ROUTER_INIT = "Starting up router...";
  private static final String ROUTER_STARTED = "Router initialised and ready to serve.";
  public static PaperStore paperStore;

  public Router() {
    paperStore = new PaperStore();
    Parser parser = new Parser(paperStore, RES_PATH);
    parser.parse();
    paperStore.update();
  }

  public void initAndServe() {
    System.out.println(ROUTER_INIT);

    // setup endpoints
    path("/api", () -> {
      path("/authors", () -> {
        get("/", AuthorController.test);
      });
    });

    System.out.println(ROUTER_STARTED);
  }

}
