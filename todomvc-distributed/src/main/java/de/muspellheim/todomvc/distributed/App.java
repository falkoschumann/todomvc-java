/*
 * TodoMVC - Distributed
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.distributed;

import com.google.gson.Gson;
import de.muspellheim.messages.Command;
import de.muspellheim.messages.CommandStatus;
import de.muspellheim.messages.Failure;
import de.muspellheim.messages.HttpCommandStatus;
import de.muspellheim.messages.Success;
import de.muspellheim.todomvc.contract.messages.queries.TodosQueryResult;
import de.muspellheim.todomvc.frontend.AboutViewController;
import de.muspellheim.todomvc.frontend.TodosViewController;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Properties;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
  private boolean useSystemMenuBar;

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void init() {
    useSystemMenuBar = getParameters().getUnnamed().contains("--useSystemMenuBar");
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    //
    // Build
    //

    var todosViewController = TodosViewController.create(primaryStage, useSystemMenuBar);

    var aboutStage = new Stage();
    aboutStage.initOwner(primaryStage);
    var aboutViewController = AboutViewController.create(aboutStage);
    var appIcon = getClass().getResource("/app.png");
    aboutViewController.setIcon(appIcon.toString());
    try (InputStream in = getClass().getResourceAsStream("/app.properties")) {
      var appProperties = new Properties();
      appProperties.load(in);
      aboutViewController.setTitle(appProperties.getProperty("title"));
      aboutViewController.setVersion(appProperties.getProperty("version"));
      aboutViewController.setCopyright(appProperties.getProperty("copyright"));
    }

    //
    // Bind
    //

    todosViewController.setOnOpenAbout(() -> aboutStage.show());
    todosViewController.setOnNewTodoCommand(
        it -> {
          var status = sendCommand("newtodocommand", it);
          if (status.equals(new Success())) {
            TodosQueryResult result = sendQuery();
            todosViewController.display(result);
          }
        });
    todosViewController.setOnToggleCommand(
        it -> {
          var status = sendCommand("togglecommand", it);
          if (status.equals(new Success())) {
            TodosQueryResult result = sendQuery();
            todosViewController.display(result);
          }
        });
    todosViewController.setOnToggleAllCommand(
        it -> {
          var status = sendCommand("toggleallcommand", it);
          if (status.equals(new Success())) {
            TodosQueryResult result = sendQuery();
            todosViewController.display(result);
          }
        });
    todosViewController.setOnEditCommand(
        it -> {
          var status = sendCommand("editcommand", it);
          if (status.equals(new Success())) {
            TodosQueryResult result = sendQuery();
            todosViewController.display(result);
          }
        });
    todosViewController.setOnDestroyCommand(
        it -> {
          var status = sendCommand("destroycommand", it);
          if (status.equals(new Success())) {
            TodosQueryResult result = sendQuery();
            todosViewController.display(result);
          }
        });
    todosViewController.setOnClearCompletedCommand(
        it -> {
          var status = sendCommand("clearcompletedcommand", it);
          if (status.equals(new Success())) {
            TodosQueryResult result = sendQuery();
            todosViewController.display(result);
          }
        });
    todosViewController.setOnTodosQuery(
        it -> {
          TodosQueryResult result = sendQuery();
          todosViewController.display(result);
        });

    //
    // Run
    //

    todosViewController.run();
  }

  private static CommandStatus sendCommand(String path, Command command) {
    try {
      var client = HttpClient.newHttpClient();
      var body = new Gson().toJson(command);
      var request =
          HttpRequest.newBuilder(URI.create("http://localhost:8080/api/" + path))
              .header("Accept", "application/json")
              .header("Content-Type", "application/json")
              .POST(BodyPublishers.ofString(body))
              .build();
      var response = client.send(request, HttpResponse.BodyHandlers.ofString());
      return new Gson().fromJson(response.body(), HttpCommandStatus.class).commandStatus();
    } catch (Exception e) {
      System.err.println(e.toString());
      return new Failure(e.getLocalizedMessage());
    }
  }

  private TodosQueryResult sendQuery() {
    try {
      var client = HttpClient.newHttpClient();
      var request =
          HttpRequest.newBuilder(URI.create("http://localhost:8080/api/todosquery"))
              .header("Accept", "application/json")
              .GET()
              .build();
      var response = client.send(request, HttpResponse.BodyHandlers.ofString());
      return new Gson().fromJson(response.body(), TodosQueryResult.class);
    } catch (Exception e) {
      System.err.println(e.toString());
      return new TodosQueryResult(List.of());
    }
  }
}
