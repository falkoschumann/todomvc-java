/*
 * TodoMVC - Demo Application
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.demo;

import de.muspellheim.todomvc.App;
import de.muspellheim.todomvc.backend.TodoRepository;
import de.muspellheim.todomvc.backend.adapters.TodoRepositoryMemory;
import de.muspellheim.todomvc.contract.data.Todo;
import java.util.List;
import javafx.application.Application;

public class DemoApp extends App {
  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  protected TodoRepository createRepository() {
    var repository = new TodoRepositoryMemory();
    repository.store(
        List.of(
            new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)));
    return repository;
  }
}
