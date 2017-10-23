package storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import models.Author;
import models.Paper;

public class PaperStore {
  private ConcurrentHashMap<String, Author> authorIdToAuthor = new ConcurrentHashMap<>(); // id -> author

  private ConcurrentHashMap<String, Paper> paperIdToPaper = new ConcurrentHashMap<>(); // id -> paper
  private ConcurrentHashMap<String, Paper> paperTitleToPaper = new ConcurrentHashMap<>(); // id -> paper

  private ConcurrentHashMap<String, List<Author>> paperIdToAuthors = new ConcurrentHashMap<>(); // paper -> authors

  private ConcurrentHashMap<String, List<Paper>> authorToPapers = new ConcurrentHashMap<>(); // author -> papers
  private ConcurrentHashMap<String, List<Paper>> keyPhraseToPapers = new ConcurrentHashMap<>(); // keyphrase -> papers
  private ConcurrentHashMap<String, List<Paper>> paperIdToInPapers = new ConcurrentHashMap<>(); // id -> paper of inCitations
  private ConcurrentHashMap<String, List<Paper>> paperIdToOutPapers = new ConcurrentHashMap<>(); // id -> paper of outCitations
  private ConcurrentHashMap<String, List<Paper>> venueToPapers = new ConcurrentHashMap<>(); // venue -> papers
  private ConcurrentHashMap<Integer, List<Paper>> yearToPapers = new ConcurrentHashMap<>(); // year -> papers

  public void addPaper(Paper paper) {
    this.paperIdToPaper.putIfAbsent(paper.getId(), paper);
    this.paperTitleToPaper.putIfAbsent(paper.getTitle(), paper);
  }

  public void update() {
    this.paperIdToPaper.values().stream().forEach(paper -> setupRelations(paper));
  }

  private void setupRelations(Paper paper) {
    buildAuthorRelations(paper);
    buildCitationsRelations(paper);
    buildKeyPhraseRelations(paper);
    buildVenueRelations(paper);
    buildYearRelations(paper);
  }

  private void buildAuthorRelations(Paper paper) {
    List<Author> authors = paper.getAuthors();
    String paperId = paper.getId();
    authors.stream().forEach(author -> {
      String authorId = author.getId();
      // track all authors
      authorIdToAuthor.putIfAbsent(authorId, author);

      // track paper to authors
      if (paperIdToAuthors.containsKey(paperId)) {
        paperIdToAuthors.get(paperId).add(author);
      } else {
        ArrayList<Author> newList = new ArrayList<>();
        newList.add(author);
        paperIdToAuthors.putIfAbsent(paperId, Collections.synchronizedList(newList));
      }

      // track author to papers
      if (authorToPapers.containsKey(authorId)) {
        authorToPapers.get(authorId).add(paper);
      } else {
        ArrayList<Paper> newList = new ArrayList<>();
        newList.add(paper);
        authorToPapers.putIfAbsent(authorId, Collections.synchronizedList(newList));
      }
    });
  }

  private void buildCitationsRelations(Paper paper) {
    List<String> inCitations = paper.getInCitations();
    Iterator<String> inCitationsIterator = inCitations.iterator();
    while (inCitationsIterator.hasNext()) {
      String inCitation = inCitationsIterator.next();
      if (paperIdToPaper.containsKey(inCitation)) {
        buildCitationRelation(paperIdToPaper.get(inCitation), paper);
      } else {
        inCitationsIterator.remove();
      }
    }

    List<String> outCitations = paper.getOutCitations();
    Iterator<String> outCitationsIterator = outCitations.iterator();
    while (outCitationsIterator.hasNext()) {
      String outCitation = outCitationsIterator.next();
      if (paperIdToPaper.containsKey(outCitation)) {
        buildCitationRelation(paper, paperIdToPaper.get(outCitation));
      } else {
        outCitationsIterator.remove();
      }
    }
  }

  private void buildCitationRelation(Paper inPaper, Paper outPaper) {
    String inPaperId = inPaper.getId();
    String outPaperId = outPaper.getId();

    // track paper to list of citing papers
    if (paperIdToInPapers.containsKey(outPaperId)) {
      paperIdToInPapers.get(outPaperId).add(inPaper);
    } else {
      ArrayList<Paper> newList = new ArrayList<>();
      newList.add(inPaper);
      paperIdToInPapers.putIfAbsent(outPaperId, Collections.synchronizedList(newList));
    }

    // track paper to list of cited papers
    if (paperIdToOutPapers.containsKey(inPaperId)) {
      paperIdToOutPapers.get(inPaperId).add(outPaper);
    } else {
      ArrayList<Paper> newList = new ArrayList<>();
      newList.add(outPaper);
      paperIdToOutPapers.putIfAbsent(inPaperId, Collections.synchronizedList(newList));
    }
  }

  private void buildKeyPhraseRelations(Paper paper) {
    List<String> keyPhrases = paper.getKeyPhrases();
    keyPhrases.stream().forEach(phrase -> {
      if (keyPhraseToPapers.containsKey(phrase)) {
        keyPhraseToPapers.get(phrase).add(paper);
      } else {
        ArrayList<Paper> newList = new ArrayList<>();
        newList.add(paper);
        keyPhraseToPapers.putIfAbsent(phrase, Collections.synchronizedList(newList));
      }
    });
  }

  private void buildVenueRelations(Paper paper) {
    String venue = paper.getVenue();
    if (venueToPapers.containsKey(venue)) {
      venueToPapers.get(venue).add(paper);
    } else {
      ArrayList<Paper> newList = new ArrayList<>();
      newList.add(paper);
      venueToPapers.putIfAbsent(venue, Collections.synchronizedList(newList));
    }
  }

  private void buildYearRelations(Paper paper) {
    Integer year = paper.getYear();
    if (yearToPapers.containsKey(year)) {
      yearToPapers.get(year).add(paper);
    } else {
      ArrayList<Paper> newList = new ArrayList<>();
      newList.add(paper);
      yearToPapers.putIfAbsent(year, Collections.synchronizedList(newList));
    }
  }

  public ConcurrentHashMap<String, Paper> getPapers() {
    return paperIdToPaper;
  }

  public ConcurrentHashMap<String, Author> getAuthors() {
    return authorIdToAuthor;
  }

  public ConcurrentHashMap<String, List<Author>> getPaperToAuthors() {
    return paperIdToAuthors;
  }

  public ConcurrentHashMap<String, List<Paper>> getAuthorToPapers() {
    return authorToPapers;
  }

  public ConcurrentHashMap<String, List<Paper>> getInCitations() {
    return paperIdToInPapers;
  }

  public ConcurrentHashMap<String, List<Paper>> getOutCitations() {
    return paperIdToOutPapers;
  }

  public ConcurrentHashMap<String, List<Paper>> getKeyPhraseToPapers() {
    return keyPhraseToPapers;
  }

  public ConcurrentHashMap<String, List<Paper>> getVenueToPapers() {
    return venueToPapers;
  }

  public ConcurrentHashMap<Integer, List<Paper>> getYearToPapers() {
    return yearToPapers;
  }
  public ConcurrentHashMap<String, Paper> getTitleToPapers() {
	    return paperTitleToPaper;
	}
}
