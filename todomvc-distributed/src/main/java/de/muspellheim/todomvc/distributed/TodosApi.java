/*
 * TodoMVC - Distributed
 * Copyright (c) 2021 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.distributed;

import com.google.gson.Gson;
import de.muspellheim.messages.Command;
import de.muspellheim.messages.CommandStatus;
import de.muspellheim.messages.Failure;
import de.muspellheim.messages.HttpCommandStatus;
import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleAllCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import de.muspellheim.todomvc.contract.messages.queries.TodosQueryResult;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.List;

class TodosApi {
  public static final String BACKEND_URL = "http://localhost:8080/api/";

  CommandStatus sendCommand(ClearCompletedCommand command) {
    return sendCommand("clear-completed-command", command);
  }

  CommandStatus sendCommand(DestroyCommand command) {
    return sendCommand("destroy-command", command);
  }

  CommandStatus sendCommand(EditCommand command) {
    return sendCommand("edit-command", command);
  }

  CommandStatus sendCommand(NewTodoCommand command) {
    return sendCommand("new-todo-command", command);
  }

  CommandStatus sendCommand(ToggleAllCommand command) {
    return sendCommand("toggle-all-command", command);
  }

  CommandStatus sendCommand(ToggleCommand command) {
    return sendCommand("toggle-command", command);
  }

  TodosQueryResult sendTodosQuery(TodosQuery query) {
    try {
      var client = HttpClient.newHttpClient();
      var request =
          HttpRequest.newBuilder(URI.create(BACKEND_URL + "todos-query"))
              .header("Accept", "application/json")
              .GET()
              .build();
      var response = client.send(request, HttpResponse.BodyHandlers.ofString());
      return new Gson().fromJson(response.body(), TodosQueryResult.class);
    } catch (Exception e) {
      e.printStackTrace();
      return new TodosQueryResult(List.of());
    }
  }

  private static CommandStatus sendCommand(String path, Command command) {
    try {
      var client = HttpClient.newHttpClient();
      var body = new Gson().toJson(command);
      var request =
          HttpRequest.newBuilder(URI.create(BACKEND_URL + path))
              .header("Accept", "application/json")
              .header("Content-Type", "application/json")
              .POST(BodyPublishers.ofString(body))
              .build();
      var response = client.send(request, HttpResponse.BodyHandlers.ofString());
      return new Gson().fromJson(response.body(), HttpCommandStatus.class).commandStatus();
    } catch (Exception e) {
      e.printStackTrace();
      return new Failure(e.getLocalizedMessage());
    }
  }
}
