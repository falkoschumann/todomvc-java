/*
 * TodoMVC - Contract
 * Copyright (c) 2021 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.contract;

import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import de.muspellheim.todomvc.contract.messages.commands.CommandStatus;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleAllCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import de.muspellheim.todomvc.contract.messages.queries.TodosQueryResult;

public interface MessageHandling {
  CommandStatus handle(ClearCompletedCommand command);

  CommandStatus handle(DestroyCommand command);

  CommandStatus handle(EditCommand command);

  CommandStatus handle(NewTodoCommand command);

  CommandStatus handle(ToggleAllCommand command);

  CommandStatus handle(ToggleCommand command);

  TodosQueryResult handle(TodosQuery query);
}
