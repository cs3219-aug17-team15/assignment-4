package controllers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
	  String result ="id,value\n" ;
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
	  final String PATH = Paths.get(System.getProperty("user.dir"), "src/main/task1.csv").toString();
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
  public String task2() {
	  String result ="id,value\n" ;
	  ConcurrentHashMap<String, List<Paper>> listVenue = paperStore.getVenueToPapers();
	  List<Paper> list = listVenue.get("arXiv");
	  ConcurrentHashMap<String, Integer> listCitation = new ConcurrentHashMap<String, Integer>();
	  String max[] = new String[5];
	  for (Paper p : list){
		  List<String> citation = p.getInCitations();
		  for (String s : citation) {
			  listCitation.putIfAbsent(s, 0);
			  int i = listCitation.get(s);
			  i += 1;
			  String temp = s;
			  for(int j = 0; j < 5; j++){
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
	  final String PATH = Paths.get(System.getProperty("user.dir"), "src/main/task2.csv").toString();
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
  public String task3() {
	  String result ="Year,Amount\n" ;
	  ConcurrentHashMap<String, List<Paper>> listVenue = paperStore.getVenueToPapers();
	  List<Paper> list = listVenue.get("arXiv");
	  ConcurrentHashMap<Integer, Integer> listYear = new ConcurrentHashMap<Integer, Integer>();
	  int largest = Integer.MIN_VALUE;
	  int smallest = Integer.MAX_VALUE;
	  for (Paper p : list){
		  int year = p.getYear();
		  listYear.putIfAbsent(year, 0);
		  int i = listYear.get(year);
		  i += 1;
		  if(year > largest){
			  largest = year;
		  }
		  if(year < smallest){
			  smallest = year;
		  }
	  }
	  for(int i = smallest; i <= largest; i++){
		  int count = listYear.get(i);
		  if(listYear.get(i) == null){
			  result += i+","+0;
		  } else {
			  result += i+","+count;
		  }
	  }
	  final String PATH = Paths.get(System.getProperty("user.dir"), "src/main/task3.csv").toString();
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
  public String task4() {
	  String result ="Task4:\n" ;
	  String listVertex = "{\n\"nodes\": [\n";
	  String listEdge = "\"links\": [\n";
	  String title = "Low-density parity check codes over GF(q)";
	  ConcurrentHashMap<String, List<Paper>> listInCitation = paperStore.getInCitations();
	  ConcurrentHashMap<String, List<Paper>> listOutCitation = paperStore.getOutCitations();
	  List<Paper> inList = listInCitation.get(title);
	  List<Paper> outList = listOutCitation.get(title);
	  Boolean first = true;
	  listVertex += "{\"id\": \""+title+"\", \"group\": "+0+"}";
	  for (Paper p : inList){
		  listVertex += ",\n{\"id\": \""+p.getTitle()+"\", \"group\": "+1+"}";
		  if(first){
			  listEdge += "{\"source\": \""+ p.getTitle()+"\", \"target\": \""+title+"\", \"value\": 1}";
			  first = false;
		  } else {
			  listEdge += ",\n{\"source\": \""+ p.getTitle()+"\", \"target\": \""+title+"\", \"value\": 1}";
		  }
		  List<Paper> list = listInCitation.get(p.getTitle());
		  for (Paper t : list){
			  listVertex += ",\n{\"id\": \""+t.getTitle()+"\", \"group\": "+3+"}";
			  listEdge += ",\n{\"source\": \""+ t.getTitle()+"\", \"target\": \""+p.getTitle()+"\", \"value\": 1}";
		  }
	  }
	  for (Paper p : outList){
		  listVertex += ",\n{\"id\": \""+p.getTitle()+"\", \"group\": "+2+"}";
		  listEdge += ",\n{\"source\": \""+title+"\", \"target\": \""+p.getTitle()+"\", \"value\": 1}";
		  List<Paper> list = listOutCitation.get(p.getTitle());
		  for (Paper t : list){
			  listVertex += ",\n{\"id\": \""+t.getTitle()+"\", \"group\": "+4+"}";
			  listEdge += ",\n{\"source\": \""+p.getTitle()+"\", \"target\": \""+t.getTitle()+"\", \"value\": 1}";
		  }
	  }
	  result = listVertex+"\n],\n" + listEdge+"\n]\n}\n";
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
  public String task5() {
	  String result ="id,value\n" ;
	  ConcurrentHashMap<String, List<Paper>> listVenue = paperStore.getVenueToPapers();
	  List<Paper> list = listVenue.get("arXiv");
	  ConcurrentHashMap<String, Integer> listKeyPhrase = new ConcurrentHashMap<String, Integer>();
	  String max[] = new String[10];
	  for (Paper p : list){
		  List<String> phrase = p.getKeyPhrases();
		  for (String s : phrase) {
			  listKeyPhrase.putIfAbsent(s, 0);
			  int i = listKeyPhrase.get(s);
			  i += 1;
			  String temp = s;
			  for(int j = 0; j < 10; j++){
				  if(max[j] == null || listKeyPhrase.get(max[j]) < listKeyPhrase.get(temp)){
					  String temp1 = max[j];
					  max[j] = temp;
					  temp = temp1;
				  }
			  }
		  }
	  }
	  for (String s : max){
		  result += s +","+listKeyPhrase.get(s)+"\n";
	  }
	  final String PATH = Paths.get(System.getProperty("user.dir"), "src/main/task5.csv").toString();
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
}
