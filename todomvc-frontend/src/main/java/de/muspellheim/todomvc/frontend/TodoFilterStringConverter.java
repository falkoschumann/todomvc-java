/*
 * TodoMVC - Frontend
 * Copyright (c) 2021 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.frontend;

import javafx.util.StringConverter;

public class TodoFilterStringConverter extends StringConverter<TodoFilter> {

  @Override
  public String toString(TodoFilter object) {
    switch (object) {
      case ALL:
        return "All";
      case ACTIVE:
        return "Active";
      case COMPLETED:
        return "Completed";
      default:
        throw new IllegalArgumentException("Unreachable code");
    }
  }

  @Override
  public TodoFilter fromString(String string) {
    return TodoFilter.valueOf(string);
  }
}
