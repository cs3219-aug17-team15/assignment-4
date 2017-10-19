package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import models.Paper;
import storage.PaperStore;

public class Parser {
  private static final String ERROR_FILE_NOT_FOUND = "ERROR: Resource file not found!";
  private static final String ERROR_FILE_PARSE = "ERROR: Parsing file!";

  private Gson gson;
  private PaperStore paperStore;
  private String resPath;

  public Parser(PaperStore paperStore, String filePath) {
    this.gson = new GsonBuilder().create();
    this.paperStore = paperStore;
    this.resPath = filePath;
  }

  public void parse() {
    try {
      File file = new File(this.resPath);
      BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        this.paperStore.addPaper(parseToPaper(line));
      }
      bufferedReader.close();
    } catch (FileNotFoundException e) {
      System.err.println(ERROR_FILE_NOT_FOUND);
    } catch (IOException e) {
      System.err.println(ERROR_FILE_PARSE);
    }
  }

  public Paper parseToPaper(String jsonString) {
    return gson.fromJson(jsonString, Paper.class);
  }
}
