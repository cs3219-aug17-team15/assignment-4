package controllers;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import models.Author;
import models.Paper;
import parser.Parser;
import spark.Request;
import spark.Response;
import spark.Route;
import storage.PaperStore;

public class LogicController {
  // replace resource.json with the file name of the dataset
  private static final String RES_PATH =
      Paths.get(System.getProperty("user.dir"), "assets/resource.json").toString();

  private static LogicController logicController;
  private static PaperStore paperStore;
  private static Gson gson;

  protected LogicController() {
    PaperStore paperStore = new PaperStore();
    Parser parser = new Parser(paperStore, RES_PATH);
    parser.parse();
    paperStore.update();
    LogicController.paperStore = paperStore;

    gson = new Gson();
  }

  public static LogicController getInstance() {
    if (logicController == null) {
      logicController = new LogicController();
    }
    return logicController;
  }

  public static Route task1JSON = (Request req, Response resp) -> {
    List<Paper> list = paperStore.getVenueToPapers().getOrDefault("ArXiv", new ArrayList<>());
    ConcurrentHashMap<String, Integer> listAuthor = new ConcurrentHashMap<String, Integer>();
    String authorName;
    for (Paper p : list) {
      for (Author a : p.getAuthors()) {
        authorName = a.getName();
        if (listAuthor.containsKey(a.getName())) {
          listAuthor.put(authorName, listAuthor.get(authorName) + 1);
        } else {
          listAuthor.put(authorName, 1);
        }
      }
    }
    return gson.toJson(findGreatest(listAuthor, 10).stream()
        .map(entry -> new NameCount(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList()));
  };

  public static Route task2JSON = (Request req, Response resp) -> {
    List<Paper> list = paperStore.getVenueToPapers().getOrDefault("ArXiv", new ArrayList<>());
    ConcurrentHashMap<String, Integer> listCitation = new ConcurrentHashMap<String, Integer>();
    String paperTitle;
    for (Paper p : list) {
      paperTitle = p.getTitle();
      List<String> citation = p.getInCitations();
      if (listCitation.containsKey(paperTitle)) {
        listCitation.put(paperTitle, listCitation.get(paperTitle) + citation.size());
      } else {
        listCitation.put(paperTitle, citation.size());
      }
    }
    return gson.toJson(findGreatest(listCitation, 5).stream()
        .map(entry -> new NameCount(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList()));
  };

  public static Route task3JSON = (Request req, Response resp) -> {
    ConcurrentHashMap<String, List<Paper>> listVenue = paperStore.getVenueToPapers();
    List<Paper> list = listVenue.get("ICSE");
    ConcurrentHashMap<Integer, Integer> listYear = new ConcurrentHashMap<Integer, Integer>();
    int year;
    for (Paper p : list) {
      year = p.getYear();
      if (listYear.containsKey(year)) {
        listYear.put(year, listYear.get(year) + 1);
      } else {
        listYear.put(year, 1);
      }
    }
    return gson.toJson(listYear.entrySet().stream()
        .map(entry -> new NameCount(Integer.toString(entry.getKey()), entry.getValue()))
        .collect(Collectors.toList()));
  };

  public String task4() {
    String result = "Task4:\n";
    String listVertex = "";
    String listEdge = "";
    String title = "Low-density parity check codes over GF(q)";
    ConcurrentHashMap<String, List<Paper>> listInCitation = paperStore.getInCitations();
    ConcurrentHashMap<String, List<Paper>> listOutCitation = paperStore.getOutCitations();
    List<Paper> inList = listInCitation.get(title);
    List<Paper> outList = listOutCitation.get(title);
    listVertex += title + "," + "0";
    for (Paper p : inList) {
      listVertex += p.getTitle() + "," + "1";
      listEdge += p.getTitle() + "-" + title;
      List<Paper> list = listInCitation.get(p.getTitle());
      for (Paper t : list) {
        listVertex += t.getTitle() + "," + "3";
        listEdge += t.getTitle() + "-" + p.getTitle();
      }
    }
    for (Paper p : outList) {
      listVertex += p.getTitle() + "," + "2";
      listEdge += title + "-" + p.getTitle();
      List<Paper> list = listOutCitation.get(p.getTitle());
      for (Paper t : list) {
        listVertex += t.getTitle() + "," + "4";
        listEdge += p.getTitle() + "-" + t.getTitle();
      }
    }
    return result = listVertex + listEdge;
  }

  public String task5() {
    String result = "Task5:\n";
    ConcurrentHashMap<String, List<Paper>> listVenue = paperStore.getVenueToPapers();
    List<Paper> list = listVenue.get("ArXiv");
    ConcurrentHashMap<String, Integer> listKeyPhrase = new ConcurrentHashMap<String, Integer>();
    String max[] = new String[10];
    for (Paper p : list) {
      List<String> phrase = p.getKeyPhrases();
      for (String s : phrase) {
        listKeyPhrase.putIfAbsent(s, 0);
        int i = listKeyPhrase.get(s);
        i += 1;
        String temp = s;
        for (int j = 0; j < 10; j++) {
          if (max[j] == null || listKeyPhrase.get(max[j]) < listKeyPhrase.get(temp)) {
            String temp1 = max[j];
            max[j] = temp;
            temp = temp1;
          }
        }
      }
    }
    for (String s : max) {
      result += s + "," + listKeyPhrase.get(s) + "\n";
    }
    return result;
  }

  protected static <K, V extends Comparable<? super V>> List<Entry<K, V>> findGreatest(
      Map<K, V> map, int n) {
    Comparator<? super Entry<K, V>> comparator = new Comparator<Entry<K, V>>() {
      @Override
      public int compare(Entry<K, V> e0, Entry<K, V> e1) {
        V v0 = e0.getValue();
        V v1 = e1.getValue();
        return v0.compareTo(v1);
      }
    };
    PriorityQueue<Entry<K, V>> highest = new PriorityQueue<Entry<K, V>>(n, comparator);
    for (Entry<K, V> entry : map.entrySet()) {
      highest.offer(entry);
      while (highest.size() > n) {
        highest.poll();
      }
    }

    List<Entry<K, V>> result = new ArrayList<Map.Entry<K, V>>();
    while (highest.size() > 0) {
      result.add(highest.poll());
    }
    return result;
  }

  private static class NameCount {
    private int count;
    private String name;

    public NameCount(String name, int count) {
      this.count = count;
      this.name = name;
    }
  }
}
