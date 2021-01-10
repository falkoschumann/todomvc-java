/*
 * TodoMVC - Frontend
 * Copyright (c) 2021 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.frontend;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

@SuppressWarnings("rawtypes")
public class TodoListCellFactory implements Callback<ListView, ListCell> {
  @Override
  public ListCell call(ListView view) {
    return new TodoListCell<>();
  }
}
