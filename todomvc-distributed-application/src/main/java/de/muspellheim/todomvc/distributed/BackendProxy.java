/*
 * TodoMVC - Distributed Application
 * Copyright (c) 2021 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.distributed;

import de.muspellheim.messages.CommandStatus;
import de.muspellheim.messages.Failure;
import de.muspellheim.messages.HttpCommandStatus;
import de.muspellheim.todomvc.contract.MessageHandling;
import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleAllCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import de.muspellheim.todomvc.contract.messages.queries.TodosQueryResult;
import java.util.List;

class BackendProxy implements MessageHandling {
  public static final String BACKEND_API_BASE_URL = "http://localhost:8080/api/";

  private final HttpJsonClient client = new HttpJsonClient(BACKEND_API_BASE_URL);

  @Override
  public CommandStatus handle(ClearCompletedCommand command) {
    try {
      return client
          .execute("clear-completed-command", command, HttpCommandStatus.class)
          .commandStatus();
    } catch (Exception e) {
      return new Failure(e.getLocalizedMessage());
    }
  }

  @Override
  public CommandStatus handle(DestroyCommand command) {
    try {
      return client.execute("destroy-command", command, HttpCommandStatus.class).commandStatus();
    } catch (Exception e) {
      return new Failure(e.getLocalizedMessage());
    }
  }

  @Override
  public CommandStatus handle(EditCommand command) {
    try {
      return client.execute("edit-command", command, HttpCommandStatus.class).commandStatus();
    } catch (Exception e) {
      return new Failure(e.getLocalizedMessage());
    }
  }

  @Override
  public CommandStatus handle(NewTodoCommand command) {
    try {
      return client.execute("new-todo-command", command, HttpCommandStatus.class).commandStatus();
    } catch (Exception e) {
      return new Failure(e.getLocalizedMessage());
    }
  }

  @Override
  public CommandStatus handle(ToggleAllCommand command) {
    try {
      return client.execute("toggle-all-command", command, HttpCommandStatus.class).commandStatus();
    } catch (Exception e) {
      return new Failure(e.getLocalizedMessage());
    }
  }

  @Override
  public CommandStatus handle(ToggleCommand command) {
    try {
      return client.execute("toggle-command", command, HttpCommandStatus.class).commandStatus();
    } catch (Exception e) {
      return new Failure(e.getLocalizedMessage());
    }
  }

  @Override
  public TodosQueryResult handle(TodosQuery query) {
    try {
      return client.execute("todos-query", query, TodosQueryResult.class);
    } catch (Exception e) {
      return new TodosQueryResult(List.of());
    }
  }
}
