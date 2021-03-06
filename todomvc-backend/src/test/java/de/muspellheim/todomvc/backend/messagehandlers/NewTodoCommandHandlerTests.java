/*
 * TodoMVC - Backend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.messagehandlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import de.muspellheim.todomvc.backend.adapters.MemoryTodoRepository;
import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.Success;
import java.util.List;
import org.junit.jupiter.api.Test;

public class NewTodoCommandHandlerTests {
  @Test
  void newTodo() {
    var repository = new MemoryTodoRepository();
    repository.store(
        List.of(
            new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)));
    var messageHandler = new NewTodoCommandHandler(repository);

    var command = new NewTodoCommand("Foobar");
    var result = messageHandler.handle(command);

    assertEquals(new Success(), result, "Command handled successfully");
    assertEquals(3, repository.load().size(), "Todo added to list");
    assertNotNull(repository.load().get(2).getId(), "Todo id is set");
    assertEquals("Foobar", repository.load().get(2).getTitle(), "Todo title is set");
    assertFalse(repository.load().get(2).isCompleted(), "Todo is not completed");
  }
}
