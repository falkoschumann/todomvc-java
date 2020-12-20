/*
 * TodoMVC - Distributed
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.distributed;

import com.google.gson.Gson;
import de.muspellheim.todomvc.contract.messages.Command;
import de.muspellheim.todomvc.contract.messages.CommandStatus;
import de.muspellheim.todomvc.contract.messages.Failure;
import de.muspellheim.todomvc.contract.messages.HttpCommandStatus;
import de.muspellheim.todomvc.contract.messages.Success;
import de.muspellheim.todomvc.contract.messages.queries.TodosQueryResult;
import de.muspellheim.todomvc.frontend.TodoAppView;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage stage) {
    var frontend = new TodoAppView();

    frontend.setOnNewTodoCommand(
        it -> {
          var status = sendCommand("newtodocommand", it);
          if (status.equals(new Success())) {
            TodosQueryResult result = sendQuery();
            frontend.display(result);
          }
        });
    frontend.setOnToggleCommand(
        it -> {
          var status = sendCommand("togglecommand", it);
          if (status.equals(new Success())) {
            TodosQueryResult result = sendQuery();
            frontend.display(result);
          }
        });
    frontend.setOnToggleAllCommand(
        it -> {
          var status = sendCommand("toggleallcommand", it);
          if (status.equals(new Success())) {
            TodosQueryResult result = sendQuery();
            frontend.display(result);
          }
        });
    frontend.setOnEditCommand(
        it -> {
          var status = sendCommand("editcommand", it);
          if (status.equals(new Success())) {
            TodosQueryResult result = sendQuery();
            frontend.display(result);
          }
        });
    frontend.setOnDestroyCommand(
        it -> {
          var status = sendCommand("destroycommand", it);
          if (status.equals(new Success())) {
            TodosQueryResult result = sendQuery();
            frontend.display(result);
          }
        });
    frontend.setOnClearCompletedCommand(
        it -> {
          var status = sendCommand("clearcompletedcommand", it);
          if (status.equals(new Success())) {
            TodosQueryResult result = sendQuery();
            frontend.display(result);
          }
        });
    frontend.setOnTodosQuery(
        it -> {
          TodosQueryResult result = sendQuery();
          frontend.display(result);
        });

    frontend.run();

    Scene scene = new Scene(frontend);
    stage.setScene(scene);
    stage.setTitle("TodoMVC");
    stage.show();
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
