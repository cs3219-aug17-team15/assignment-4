package parser;

import java.awt.print.Paper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Parser {
  private Gson gson;

  protected Parser() {
    this.gson = new GsonBuilder().create();
  }

  protected ArrayList<Paper> parseFile(File jsonFile) throws IOException {
    ArrayList<Paper> results = new ArrayList<Paper>();
    BufferedReader bufferedReader = new BufferedReader(new FileReader(jsonFile));
    String line;
    while ((line = bufferedReader.readLine()) != null) {
      results.add(parseToPaper(line));
    }
    bufferedReader.close();
    return results;
  }

  protected Paper parseToPaper(String jsonString) {
    return gson.fromJson(jsonString, Paper.class);
  }
}
