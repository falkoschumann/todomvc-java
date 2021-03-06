/*
 * TodoMVC - Backend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.messagehandlers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.muspellheim.todomvc.backend.adapters.MemoryTodoRepository;
import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import de.muspellheim.todomvc.contract.messages.commands.Success;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ClearCompletedCommandHandlerTests {
  @Test
  void clearCompleted() {
    var repository = new MemoryTodoRepository();
    repository.store(
        List.of(
            new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)));
    var messageHandler = new ClearCompletedCommandHandler(repository);

    var command = new ClearCompletedCommand();
    var result = messageHandler.handle(command);

    assertEquals(new Success(), result, "Command handled successfully");
    assertEquals(
        List.of(new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)),
        repository.load(),
        "Todos updated");
  }
}
