/*
 * TodoMVC - Backend
 * Copyright (c) 2021 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend;

import de.muspellheim.messages.CommandStatus;
import de.muspellheim.todomvc.backend.messagehandlers.ClearCompletedCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.DestroyCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.EditCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.NewTodoCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.TodosQueryHandler;
import de.muspellheim.todomvc.backend.messagehandlers.ToggleAllCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.ToggleCommandHandler;
import de.muspellheim.todomvc.contract.MessageHandling;
import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleAllCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import de.muspellheim.todomvc.contract.messages.queries.TodosQueryResult;

public class MessageHandlingImpl implements MessageHandling {
  private final ClearCompletedCommandHandler clearCompletedCommandHandler;
  private final DestroyCommandHandler destroyCommandHandler;
  private final EditCommandHandler editCommandHandler;
  private final NewTodoCommandHandler newTodoCommandHandler;
  private final ToggleAllCommandHandler toggleAllCommandHandler;
  private final ToggleCommandHandler toggleCommandHandler;
  private final TodosQueryHandler todosQueryHandler;

  public MessageHandlingImpl(TodoRepository todoRepository) {
    clearCompletedCommandHandler = new ClearCompletedCommandHandler(todoRepository);
    destroyCommandHandler = new DestroyCommandHandler(todoRepository);
    editCommandHandler = new EditCommandHandler(todoRepository);
    newTodoCommandHandler = new NewTodoCommandHandler(todoRepository);
    toggleAllCommandHandler = new ToggleAllCommandHandler(todoRepository);
    toggleCommandHandler = new ToggleCommandHandler(todoRepository);
    todosQueryHandler = new TodosQueryHandler(todoRepository);
  }

  @Override
  public CommandStatus handle(ClearCompletedCommand command) {
    return clearCompletedCommandHandler.handle(command);
  }

  @Override
  public CommandStatus handle(DestroyCommand command) {
    return destroyCommandHandler.handle(command);
  }

  @Override
  public CommandStatus handle(EditCommand command) {
    return editCommandHandler.handle(command);
  }

  @Override
  public CommandStatus handle(NewTodoCommand command) {
    return newTodoCommandHandler.handle(command);
  }

  @Override
  public CommandStatus handle(ToggleAllCommand command) {
    return toggleAllCommandHandler.handle(command);
  }

  @Override
  public CommandStatus handle(ToggleCommand command) {
    return toggleCommandHandler.handle(command);
  }

  @Override
  public TodosQueryResult handle(TodosQuery query) {
    return todosQueryHandler.handle(query);
  }
}
