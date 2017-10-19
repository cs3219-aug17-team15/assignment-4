package controllers;

import java.nio.file.Paths;

import parser.Parser;
import storage.PaperStore;

public class LogicController {
  // replace resource.json with the file name of the dataset
  private static final String RES_PATH =
      Paths.get(System.getProperty("user.dir"), "assets/resource.json").toString();

  private static LogicController logicController;
  private static PaperStore paperStore;

  protected LogicController() {
    PaperStore paperStore = new PaperStore();
    Parser parser = new Parser(paperStore, RES_PATH);
    parser.parse();
    paperStore.update();
    LogicController.paperStore = paperStore;
  }

  public static LogicController getInstance() {
    if (logicController == null) {
      logicController = new LogicController();
    }
    return logicController;
  }
}
