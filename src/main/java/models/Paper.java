package models;

import java.util.List;
import java.util.Objects;

public class Paper {
  private int year;

  private String id;
  private String paperAbstract;
  private String s2Url;
  private String title;
  private String venue;

  private List<Author> authors;

  private List<String> inCitations;
  private List<String> keyPhrases;
  private List<String> outCitations;
  private List<String> pdfUrls;

  public List<Author> getAuthors() {
    return authors;
  }

  public String getId() {
    return id;
  }

  public List<String> getInCitations() {
    return inCitations;
  }

  public List<String> getKeyPhrases() {
    return keyPhrases;
  }

  public List<String> getOutCitations() {
    return outCitations;
  }

  public String getPaperAbstract() {
    return paperAbstract;
  }

  public List<String> getPdfUrls() {
    return pdfUrls;
  }

  public String getS2Url() {
    return s2Url;
  }

  public String getTitle() {
    return title;
  }

  public String getVenue() {
    return venue;
  }

  public int getYear() {
    return year;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Paper)) {
      return false;
    }
    Paper paper = (Paper) o;
    return getYear() == paper.getYear() && Objects.equals(getAuthors(), paper.getAuthors())
        && Objects.equals(getId(), paper.getId())
        && Objects.equals(getInCitations(), paper.getInCitations())
        && Objects.equals(getKeyPhrases(), paper.getKeyPhrases())
        && Objects.equals(getOutCitations(), paper.getOutCitations())
        && Objects.equals(getPaperAbstract(), paper.getPaperAbstract())
        && Objects.equals(getPdfUrls(), paper.getPdfUrls())
        && Objects.equals(getS2Url(), paper.getS2Url())
        && Objects.equals(getTitle(), paper.getTitle())
        && Objects.equals(getVenue(), paper.getVenue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getAuthors(), getId(), getInCitations(), getKeyPhrases(), getOutCitations(),
        getPaperAbstract(), getPdfUrls(), getS2Url(), getTitle(), getVenue(), getYear());
  }

}
