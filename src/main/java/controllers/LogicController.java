package controllers;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import models.Author;
import models.Paper;
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
  
  public String task1() {
	  String result ="Task1:\n" ;
	  ConcurrentHashMap<String, List<Paper>> listVenue = paperStore.getVenueToPapers();
	  List<Paper> list = listVenue.get("arXiv");
	  ConcurrentHashMap<String, Integer> listAuthor = new ConcurrentHashMap<String, Integer>();
	  String max[] = new String[10];
	  for (Paper p : list){
		  List<Author> author = p.getAuthors();
		  for (Author a : author) {
			  listAuthor.putIfAbsent(a.getName(), 0);
			  int i = listAuthor.get(a.getName());
			  i += 1;
			  String temp = a.getName();
			  for(int j = 0; j < 10; j++){
				  if(max[j] == null || listAuthor.get(max[j]) < listAuthor.get(temp)){
					  String temp1 = max[j];
					  max[j] = temp;
					  temp = temp1;
				  }
			  }
		  }
	  }
	  for (String s : max){
		  result += s +","+listAuthor.get(s)+"\n";
	  }
	  return result;
  }
  public String task2() {
	  String result ="Task2:\n" ;
	  ConcurrentHashMap<String, List<Paper>> listVenue = paperStore.getVenueToPapers();
	  List<Paper> list = listVenue.get("arXiv");
	  ConcurrentHashMap<String, Integer> listCitation = new ConcurrentHashMap<String, Integer>();
	  String max[] = new String[10];
	  for (Paper p : list){
		  List<String> citation = p.getInCitations();
		  for (String s : citation) {
			  listCitation.putIfAbsent(s, 0);
			  int i = listCitation.get(s);
			  i += 1;
			  String temp = s;
			  for(int j = 0; j < 10; j++){
				  if(max[j] == null || listCitation.get(max[j]) < listCitation.get(temp)){
					  String temp1 = max[j];
					  max[j] = temp;
					  temp = temp1;
				  }
			  }
		  }
	  }
	  for (String s : max){
		  result += s +","+listCitation.get(s)+"\n";
	  }
	  return result;
  }
  public String task3() {
	  String result ="Task3:\n" ;
	  return result;
  }
  public String task4() {
	  String result ="Task4:\n" ;
	  return result;
  }
  public String task5() {
	  String result ="Task5:\n" ;
	  return result;
  }
  public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
	  List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
	  Collections.sort( list, new Comparator<Map.Entry<K, V>>() {
	      @Override
	      public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
	          return (o1.getValue()).compareTo(o2.getValue());
	      }
	  });
	
	  Map<K, V> result = new LinkedHashMap<>();
	  for (Map.Entry<K, V> entry : list) {
	      result.put(entry.getKey(), entry.getValue());
	  }
	  return result;
  }
}
