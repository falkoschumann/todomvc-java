/*
 * TodoMVC - Frontend
 * Copyright (c) 2021 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.frontend;

import de.muspellheim.todomvc.contract.MessageHandling;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import java.net.URL;
import java.util.Properties;
import javafx.stage.Stage;

public class UserInterface {
  private final TodosViewController todosViewController;

  public UserInterface(
      MessageHandling messageHandling, Stage primaryStage, URL appIcon, Properties appProperties) {
    todosViewController = TodosViewController.create(primaryStage);

    var infoStage = new Stage();
    infoStage.initOwner(primaryStage);
    var infoViewController = InfoViewController.create(infoStage);
    infoViewController.setIcon(appIcon.toString());
    infoViewController.setTitle(appProperties.getProperty("title"));
    infoViewController.setVersion(appProperties.getProperty("version"));
    infoViewController.setCopyright(appProperties.getProperty("copyright"));

    todosViewController.setOnOpenInfo(() -> infoStage.show());
    todosViewController.setOnNewTodoCommand(
        it -> {
          messageHandling.handle(it);
          var result = messageHandling.handle(new TodosQuery());
          todosViewController.display(result);
        });
    todosViewController.setOnToggleAllCommand(
        it -> {
          messageHandling.handle(it);
          var result = messageHandling.handle(new TodosQuery());
          todosViewController.display(result);
        });
    todosViewController.setOnToggleCommand(
        it -> {
          messageHandling.handle(it);
          var result = messageHandling.handle(new TodosQuery());
          todosViewController.display(result);
        });
    todosViewController.setOnDestroyCommand(
        it -> {
          messageHandling.handle(it);
          var result = messageHandling.handle(new TodosQuery());
          todosViewController.display(result);
        });
    todosViewController.setOnEditCommand(
        it -> {
          messageHandling.handle(it);
          var result = messageHandling.handle(new TodosQuery());
          todosViewController.display(result);
        });
    todosViewController.setOnClearCompletedCommand(
        it -> {
          messageHandling.handle(it);
          var result = messageHandling.handle(new TodosQuery());
          todosViewController.display(result);
        });
    todosViewController.setOnTodosQuery(
        it -> {
          var result = messageHandling.handle(new TodosQuery());
          todosViewController.display(result);
        });
  }

  public void run() {
    todosViewController.run();
  }
}
