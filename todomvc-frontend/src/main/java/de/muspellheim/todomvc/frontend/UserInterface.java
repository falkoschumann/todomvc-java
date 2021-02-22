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
  private final MainViewController mainViewController;

  public UserInterface(
      MessageHandling messageHandling, Stage primaryStage, URL appIcon, Properties appProperties) {
    mainViewController = MainViewController.create(primaryStage);

    var infoStage = new Stage();
    infoStage.initOwner(primaryStage);
    var infoViewController = InfoViewController.create(infoStage);
    infoViewController.setIcon(appIcon.toString());
    infoViewController.setTitle(appProperties.getProperty("title"));
    infoViewController.setVersion(appProperties.getProperty("version"));
    infoViewController.setCopyright(appProperties.getProperty("copyright"));

    mainViewController.setOnOpenInfo(() -> infoStage.show());
    mainViewController.setOnNewTodoCommand(
        it -> {
          messageHandling.handle(it);
          var result = messageHandling.handle(new TodosQuery());
          mainViewController.display(result);
        });
    mainViewController.setOnToggleAllCommand(
        it -> {
          messageHandling.handle(it);
          var result = messageHandling.handle(new TodosQuery());
          mainViewController.display(result);
        });
    mainViewController.setOnToggleCommand(
        it -> {
          messageHandling.handle(it);
          var result = messageHandling.handle(new TodosQuery());
          mainViewController.display(result);
        });
    mainViewController.setOnDestroyCommand(
        it -> {
          messageHandling.handle(it);
          var result = messageHandling.handle(new TodosQuery());
          mainViewController.display(result);
        });
    mainViewController.setOnEditCommand(
        it -> {
          messageHandling.handle(it);
          var result = messageHandling.handle(new TodosQuery());
          mainViewController.display(result);
        });
    mainViewController.setOnClearCompletedCommand(
        it -> {
          messageHandling.handle(it);
          var result = messageHandling.handle(new TodosQuery());
          mainViewController.display(result);
        });
    mainViewController.setOnTodosQuery(
        it -> {
          var result = messageHandling.handle(new TodosQuery());
          mainViewController.display(result);
        });
  }

  public void run() {
    mainViewController.run();
  }
}
