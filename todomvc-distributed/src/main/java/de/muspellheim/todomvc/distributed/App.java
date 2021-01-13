/*
 * TodoMVC - Distributed
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.distributed;

import de.muspellheim.messages.Success;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import de.muspellheim.todomvc.contract.messages.queries.TodosQueryResult;
import de.muspellheim.todomvc.frontend.InfoViewController;
import de.muspellheim.todomvc.frontend.TodosViewController;
import java.io.InputStream;
import java.util.Properties;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    //
    // Build
    //

    var todosViewController = TodosViewController.create(primaryStage);

    var infoStage = new Stage();
    infoStage.initOwner(primaryStage);
    var infoViewController = InfoViewController.create(infoStage);
    var appIcon = getClass().getResource("/app.png");
    infoViewController.setIcon(appIcon.toString());
    try (InputStream in = getClass().getResourceAsStream("/app.properties")) {
      var appProperties = new Properties();
      appProperties.load(in);
      infoViewController.setTitle(appProperties.getProperty("title"));
      infoViewController.setVersion(appProperties.getProperty("version"));
      infoViewController.setCopyright(appProperties.getProperty("copyright"));
    }

    var todosApi = new TodosApi();

    //
    // Bind
    //

    todosViewController.setOnOpenInfo(() -> infoStage.show());
    todosViewController.setOnNewTodoCommand(
        it -> {
          var status = todosApi.sendCommand(it);
          if (status.equals(new Success())) {
            TodosQueryResult result = todosApi.sendTodosQuery(new TodosQuery());
            todosViewController.display(result);
          }
        });
    todosViewController.setOnToggleCommand(
        it -> {
          var status = todosApi.sendCommand(it);
          if (status.equals(new Success())) {
            TodosQueryResult result = todosApi.sendTodosQuery(new TodosQuery());
            todosViewController.display(result);
          }
        });
    todosViewController.setOnToggleAllCommand(
        it -> {
          var status = todosApi.sendCommand(it);
          if (status.equals(new Success())) {
            TodosQueryResult result = todosApi.sendTodosQuery(new TodosQuery());
            todosViewController.display(result);
          }
        });
    todosViewController.setOnEditCommand(
        it -> {
          var status = todosApi.sendCommand(it);
          if (status.equals(new Success())) {
            TodosQueryResult result = todosApi.sendTodosQuery(new TodosQuery());
            todosViewController.display(result);
          }
        });
    todosViewController.setOnDestroyCommand(
        it -> {
          var status = todosApi.sendCommand(it);
          if (status.equals(new Success())) {
            TodosQueryResult result = todosApi.sendTodosQuery(new TodosQuery());
            todosViewController.display(result);
          }
        });
    todosViewController.setOnClearCompletedCommand(
        it -> {
          var status = todosApi.sendCommand(it);
          if (status.equals(new Success())) {
            TodosQueryResult result = todosApi.sendTodosQuery(new TodosQuery());
            todosViewController.display(result);
          }
        });
    todosViewController.setOnTodosQuery(
        it -> {
          TodosQueryResult result = todosApi.sendTodosQuery(it);
          todosViewController.display(result);
        });

    //
    // Run
    //

    todosViewController.run();
  }
}
