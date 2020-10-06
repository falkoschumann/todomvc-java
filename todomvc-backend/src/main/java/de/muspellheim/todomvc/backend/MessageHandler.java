/*
 * TodoMVC - Backend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend;

import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import de.muspellheim.todomvc.contract.messages.commands.CommandStatus;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import de.muspellheim.todomvc.contract.messages.commands.Failure;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.Success;
import de.muspellheim.todomvc.contract.messages.commands.ToggleAllCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import de.muspellheim.todomvc.contract.messages.queries.TodoListQuery;
import de.muspellheim.todomvc.contract.messages.queries.TodoListQueryResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.val;
import lombok.var;

public class MessageHandler {
  private final TodoRepository repository;

  public MessageHandler(TodoRepository repository) {
    this.repository = repository;
  }

  public CommandStatus handle(NewTodoCommand command) {
    try {
      val todos = new ArrayList<>(repository.load());
      todos.add(Todo.of(command.getTitle()));
      repository.store(todos);
      return new Success();
    } catch (IOException e) {
      return new Failure(e.getLocalizedMessage());
    }
  }

  public CommandStatus handle(ToggleAllCommand command) {
    try {
      var todos = repository.load();
      todos =
          todos.stream()
              .map(it -> it.withCompleted(command.isCompleted()))
              .collect(Collectors.toList());
      repository.store(todos);
      return new Success();
    } catch (IOException e) {
      return new Failure(e.getLocalizedMessage());
    }
  }

  public CommandStatus handle(ToggleCommand command) {
    try {
      var todos = repository.load();
      todos =
          todos.stream()
              .map(
                  it ->
                      (it.getId().equals(command.getId()))
                          ? it.withCompleted(!it.isCompleted())
                          : it)
              .collect(Collectors.toList());
      repository.store(todos);
      return new Success();
    } catch (IOException e) {
      return new Failure(e.getLocalizedMessage());
    }
  }

  public CommandStatus handle(DestroyCommand command) {
    try {
      var todos = repository.load();
      todos =
          todos.stream().filter(it -> it.getId() != command.getId()).collect(Collectors.toList());
      repository.store(todos);
      return new Success();
    } catch (IOException e) {
      return new Failure(e.getLocalizedMessage());
    }
  }

  public CommandStatus handle(EditCommand command) {
    try {
      var todos = repository.load();
      todos =
          todos.stream()
              .map(it -> it.getId().equals(command.getId()) ? it.withTitle(command.getTitle()) : it)
              .collect(Collectors.toList());
      repository.store(todos);
      return new Success();
    } catch (IOException e) {
      return new Failure(e.getLocalizedMessage());
    }
  }

  public CommandStatus handle(ClearCompletedCommand command) {
    try {
      var todos = repository.load();
      todos = todos.stream().filter(it -> !it.isCompleted()).collect(Collectors.toList());
      repository.store(todos);
      return new Success();
    } catch (IOException e) {
      return new Failure(e.getLocalizedMessage());
    }
  }

  public TodoListQueryResult handle(TodoListQuery query) {
    try {
      val todos = repository.load();
      return new TodoListQueryResult(todos);
    } catch (IOException e) {
      System.err.println(e);
      return new TodoListQueryResult(Collections.emptyList());
    }
  }
}