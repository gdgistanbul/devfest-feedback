package com.gdgistanbul.devfest.feedback.models;

public class Session {
  public int id;
  public String title;
  public String description;

  public boolean contains(String query) {
    return ((title != null && title.toLowerCase().contains(query))
        || (description != null && description.toLowerCase().contains(query)));
  }
}
