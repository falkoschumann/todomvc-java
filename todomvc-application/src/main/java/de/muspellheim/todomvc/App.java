/*
 * TodoMVC - Application
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc;

import de.muspellheim.todomvc.backend.MessageHandler;
import de.muspellheim.todomvc.backend.TodoRepository;
import de.muspellheim.todomvc.backend.adapters.JsonTodoRepository;
import de.muspellheim.todomvc.backend.adapters.MemoryTodoRepository;
import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.frontend.UserInterface;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
  private TodoRepository repository;

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void init() throws Exception {
    var demo = getParameters().getUnnamed().contains("--demo");
    if (demo) {
      System.out.println("Run in demo mode...");
      repository = new MemoryTodoRepository();
      repository.store(
          List.of(
              new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
              new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)));
    } else {
      var file = Paths.get("todos.json");
      repository = new JsonTodoRepository(file);
    }
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    var backend = new MessageHandler(repository);
    var appIcon = getClass().getResource("/app.png");
    var appProperties = new Properties();
    try (InputStream in = getClass().getResourceAsStream("/app.properties")) {
      appProperties.load(in);
    }
    var frontend = new UserInterface(backend, primaryStage, appIcon, appProperties);
    frontend.run();
  }
}
