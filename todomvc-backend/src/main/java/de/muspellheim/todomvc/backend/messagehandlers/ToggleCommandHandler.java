/*
 * TodoMVC - Backend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.messagehandlers;

import de.muspellheim.todomvc.backend.TodoRepository;
import de.muspellheim.todomvc.contract.messages.CommandHandling;
import de.muspellheim.todomvc.contract.messages.CommandStatus;
import de.muspellheim.todomvc.contract.messages.Failure;
import de.muspellheim.todomvc.contract.messages.Success;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import java.util.stream.Collectors;
import lombok.NonNull;

public class ToggleCommandHandler implements CommandHandling<ToggleCommand> {
  private final TodoRepository repository;

  public ToggleCommandHandler(TodoRepository repository) {
    this.repository = repository;
  }

  @Override
  public CommandStatus handle(@NonNull ToggleCommand command) {
    try {
      var todos =
          repository.load().stream()
              .peek(
                  it -> {
                    if (it.getId().equals(command.getId())) {
                      it.setCompleted(!it.isCompleted());
                    }
                  })
              .collect(Collectors.toList());
      repository.store(todos);
      return new Success();
    } catch (Exception e) {
      return new Failure(e.getLocalizedMessage());
    }
  }
}
