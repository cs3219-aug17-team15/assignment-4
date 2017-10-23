package controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
    String listVertex = "{\n\"nodes\": [\n";
    String listEdge = "\"links\": [\n";
    String title = "Low-density parity check codes over GF(q)";
    ConcurrentHashMap<String, List<Paper>> listInCitation = paperStore.getInCitations();
    ConcurrentHashMap<String, List<Paper>> listOutCitation = paperStore.getOutCitations();
    ConcurrentHashMap<String, Paper> listPaper = paperStore.getTitleToPapers();
    Paper base = listPaper.get(title);
    List<Paper> inList = listInCitation.getOrDefault(base.getId(), new ArrayList<>());
    List<Paper> outList = listOutCitation.getOrDefault(base.getId(), new ArrayList<>());
    Boolean first = true;
    listVertex += "{\"id\": \"" + title + "\", \"group\": " + 0 + "}";
    for (Paper p : inList) {
      listVertex += ",\n{\"id\": \"" + p.getId() + "\", \"group\": " + 1 + "}";
      if (first) {
        listEdge +=
            "{\"source\": \"" + p.getId() + "\", \"target\": \"" + title + "\", \"value\": 1}";
        first = false;
      } else {
        listEdge += ",\n{\"source\": \"" + p.getId() + "\", \"target\": \"" + title
            + "\", \"value\": 1}";
      }
      List<Paper> list = listInCitation.getOrDefault(p.getId(),new ArrayList<>());
      for (Paper t : list) {
        listVertex += ",\n{\"id\": \"" + t.getId() + "\", \"group\": " + 3 + "}";
        listEdge += ",\n{\"source\": \"" + t.getId() + "\", \"target\": \"" + p.getId()
            + "\", \"value\": 1}";
      }
    }
    for (Paper p : outList) {
      listVertex += ",\n{\"id\": \"" + p.getId() + "\", \"group\": " + 2 + "}";
      listEdge +=
          ",\n{\"source\": \"" + title + "\", \"target\": \"" + p.getId() + "\", \"value\": 1}";
      List<Paper> list = listOutCitation.getOrDefault(p.getId(),new ArrayList<>());
      for (Paper t : list) {
        listVertex += ",\n{\"id\": \"" + t.getId() + "\", \"group\": " + 4 + "}";
        listEdge += ",\n{\"source\": \"" + p.getId() + "\", \"target\": \"" + t.getId()
            + "\", \"value\": 1}";
      }
    }
    result = listVertex + "\n],\n" + listEdge + "\n]\n}\n";
    final String PATH = Paths.get(System.getProperty("user.dir"), "src/main/task4.json").toString();
    FileWriter fw;
    try {
      fw = new FileWriter(PATH);
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(result);
      bw.close();
      fw.close();

    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    return result;
  }

  public static Route task4JSON = (Request req, Response resp) -> {
    String title = "Low-density parity check codes over GF(q)";
    ConcurrentHashMap<String, Paper> listPaper = paperStore.getPapers();

    List<Task4Node> vertices = new ArrayList<>();
    List<Task4Link> edges = new ArrayList<>();

    Paper base = paperStore.getTitleToPapers().get(title);
    vertices.add(new Task4Node(base, 0));
    Paper linkedPaper, secLinkedPaper;
    for (String baseIn : base.getInCitations()) {
      linkedPaper = listPaper.get(baseIn);
      if (linkedPaper == null) {
        continue;
      }
      vertices.add(new Task4Node(linkedPaper, 1));
      edges.add(new Task4Link(linkedPaper.getId(), base.getId(), 1));
      for (String secIn : linkedPaper.getInCitations()) {
        secLinkedPaper = listPaper.get(secIn);
        if (secLinkedPaper == null) {
          continue;
        }
        vertices.add(new Task4Node(secLinkedPaper, 2));
        edges.add(new Task4Link(secLinkedPaper.getId(), linkedPaper.getId(), 2));
      }
    }

    return gson.toJson(new Task4Data(vertices, edges));
  };

  public static Route task5JSON = (Request req, Response resp) -> {
    ConcurrentHashMap<String, List<Paper>> listVenue = paperStore.getVenueToPapers();
    List<Paper> list = listVenue.get("ArXiv");
    ConcurrentHashMap<String, Integer> listKeyPhrase = new ConcurrentHashMap<String, Integer>();
    for (Paper p : list) {
      for (String s : p.getKeyPhrases()) {
        if (listKeyPhrase.containsKey(s)) {
          listKeyPhrase.put(s, listKeyPhrase.get(s) + 1);
        } else {
          listKeyPhrase.put(s, 1);
        }
      }
    }
    return gson.toJson(findGreatest(listKeyPhrase, 10).stream()
        .map(entry -> new NameCount(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList()));
  };

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

  @SuppressWarnings(value = {"unused"})
  private static class NameCount {
    private int count;
    private String name;

    public NameCount(String name, int count) {
      this.count = count;
      this.name = name;
    }
  }

  @SuppressWarnings(value = {"unused"})
  private static class Task4Data {
    private List<Task4Link> links;
    private List<Task4Node> nodes;

    public Task4Data(List<Task4Node> nodes, List<Task4Link> links) {
      this.links = links;
      this.nodes = nodes;
    }
  }

  @SuppressWarnings(value = {"unused"})
  private static class Task4Node {
    String id;
    String title;
    String venue;
    List<Author> authors;
    int group;
    int year;

    public Task4Node(Paper p, int group) {
      this.id = p.getId();
      this.title = p.getTitle();
      this.authors = p.getAuthors();
      this.venue = p.getVenue();
      this.year = p.getYear();
      this.group = group;
    }
  }

  @SuppressWarnings(value = {"unused"})
  private static class Task4Link {
    String source;
    String target;
    int value;

    public Task4Link(String sourceId, String targetId, int value) {
      this.source = sourceId;
      this.target = targetId;
      this.value = value;
    }
  }

}
