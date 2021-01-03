/*
 * TodoMVC - Backend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.messagehandlers;

import de.muspellheim.messages.*;
import de.muspellheim.todomvc.backend.TodoRepository;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import de.muspellheim.todomvc.contract.messages.queries.TodosQueryResult;
import java.util.List;
import lombok.NonNull;

public class TodosQueryHandler implements QueryHandling<TodosQuery, TodosQueryResult> {
  private final TodoRepository repository;

  public TodosQueryHandler(TodoRepository repository) {
    this.repository = repository;
  }

  @Override
  public TodosQueryResult handle(@NonNull TodosQuery query) {
    try {
      var todos = repository.load();
      return new TodosQueryResult(todos);
    } catch (Exception e) {
      System.err.println(e);
      return new TodosQueryResult(List.of());
    }
  }
}
