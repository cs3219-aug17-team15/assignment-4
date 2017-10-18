package models;

import java.util.List;
import java.util.Objects;

public class Author {

  private List<String> ids;
  private String name;

  public List<String> getIds() {
    return ids;
  }

  public String getName() {
    return name;
  }

  public String getId() {
    if (getIds().size() <= 0) {
      return "";
    }
    return getIds().get(0);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Author)) {
      return false;
    }
    Author author = (Author) o;
    return Objects.equals(getIds(), author.getIds()) && Objects.equals(getName(), author.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getIds(), getName());
  }
}
