/*
 * TodoMVC - Frontend
 * Copyright (c) 2021 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.frontend;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.muspellheim.todomvc.contract.MessageHandling;
import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import de.muspellheim.todomvc.contract.messages.commands.CommandStatus;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.Success;
import de.muspellheim.todomvc.contract.messages.commands.ToggleAllCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import de.muspellheim.todomvc.contract.messages.queries.TodosQueryResult;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class MainViewModelTests {
  @Test
  void allActive() {
    var viewModel = new MainViewModel();
    viewModel.initMessageHandling(
        new TestingMessageHandling(
            new Todo("1", "Taste JavaScript", false), new Todo("2", "Buy a unicorn", false)));

    viewModel.updateTodos();

    assertAll(
        () -> assertTrue(viewModel.todosAvailableProperty().get(), "todos available"),
        () -> assertEquals(2, viewModel.activeTodoCountProperty().get(), "active todo count"),
        () -> assertFalse(viewModel.allCompletedProperty().get(), "all completed"),
        () -> assertTrue(viewModel.allActiveProperty().get(), "all active"),
        () -> assertEquals(TodoFilter.ALL, viewModel.filterProperty().get(), "filter"),
        () -> assertEquals("", viewModel.newTodoProperty().get(), "new todo"),
        () ->
            assertEquals(
                List.of(
                    new Todo("1", "Taste JavaScript", false),
                    new Todo("2", "Buy a unicorn", false)),
                viewModel.getFilteredTodos(),
                "filtered todos"));
  }

  @Test
  void allCompleted() {
    var viewModel = new MainViewModel();
    viewModel.initMessageHandling(
        new TestingMessageHandling(
            new Todo("1", "Taste JavaScript", true), new Todo("2", "Buy a unicorn", true)));

    viewModel.updateTodos();

    assertAll(
        () -> assertTrue(viewModel.todosAvailableProperty().get(), "todos available"),
        () -> assertEquals(0, viewModel.activeTodoCountProperty().get(), "active todo count"),
        () -> assertTrue(viewModel.allCompletedProperty().get(), "all completed"),
        () -> assertFalse(viewModel.allActiveProperty().get(), "all active"),
        () -> assertEquals(TodoFilter.ALL, viewModel.filterProperty().get(), "filter"),
        () -> assertEquals("", viewModel.newTodoProperty().get(), "new todo"),
        () ->
            assertEquals(
                List.of(
                    new Todo("1", "Taste JavaScript", true), new Todo("2", "Buy a unicorn", true)),
                viewModel.getFilteredTodos(),
                "filtered todos"));
  }

  @Test
  void noTodos() {
    var viewModel = new MainViewModel();
    viewModel.initMessageHandling(new TestingMessageHandling());

    viewModel.updateTodos();

    assertAll(
        () -> assertFalse(viewModel.todosAvailableProperty().get(), "todos available"),
        () -> assertEquals(0, viewModel.activeTodoCountProperty().get(), "active todo count"),
        () -> assertFalse(viewModel.allCompletedProperty().get(), "all completed"),
        () -> assertFalse(viewModel.allActiveProperty().get(), "all active"),
        () -> assertEquals(TodoFilter.ALL, viewModel.filterProperty().get(), "filter"),
        () -> assertEquals("", viewModel.newTodoProperty().get(), "new todo"),
        () -> assertEquals(List.of(), viewModel.getFilteredTodos(), "filtered todos"));
  }

  @Test
  void filterAll() {
    var viewModel = new MainViewModel();
    viewModel.initMessageHandling(
        new TestingMessageHandling(
            new Todo("1", "Taste JavaScript", true), new Todo("2", "Buy a unicorn", false)));

    viewModel.updateTodos();

    assertAll(
        () -> assertTrue(viewModel.todosAvailableProperty().get(), "todos available"),
        () -> assertEquals(1, viewModel.activeTodoCountProperty().get(), "active todo count"),
        () -> assertFalse(viewModel.allCompletedProperty().get(), "all completed"),
        () -> assertFalse(viewModel.allActiveProperty().get(), "all active"),
        () -> assertEquals(TodoFilter.ALL, viewModel.filterProperty().get(), "filter"),
        () -> assertEquals("", viewModel.newTodoProperty().get(), "new todo"),
        () ->
            assertEquals(
                List.of(
                    new Todo("1", "Taste JavaScript", true), new Todo("2", "Buy a unicorn", false)),
                viewModel.getFilteredTodos(),
                "filtered todos"));
  }

  @Test
  void filterActive() {
    var viewModel = new MainViewModel();
    viewModel.initMessageHandling(
        new TestingMessageHandling(
            new Todo("1", "Taste JavaScript", true), new Todo("2", "Buy a unicorn", false)));
    viewModel.updateTodos();

    viewModel.filterProperty().set(TodoFilter.ACTIVE);

    assertAll(
        () -> assertTrue(viewModel.todosAvailableProperty().get(), "todos available"),
        () -> assertEquals(1, viewModel.activeTodoCountProperty().get(), "active todo count"),
        () -> assertFalse(viewModel.allCompletedProperty().get(), "all completed"),
        () -> assertFalse(viewModel.allActiveProperty().get(), "all active"),
        () -> assertEquals(TodoFilter.ACTIVE, viewModel.filterProperty().get(), "filter"),
        () -> assertEquals("", viewModel.newTodoProperty().get(), "new todo"),
        () ->
            assertEquals(
                List.of(new Todo("2", "Buy a unicorn", false)),
                viewModel.getFilteredTodos(),
                "filtered todos"));
  }

  @Test
  void filterCompleted() {
    var viewModel = new MainViewModel();
    viewModel.initMessageHandling(
        new TestingMessageHandling(
            new Todo("1", "Taste JavaScript", true), new Todo("2", "Buy a unicorn", false)));
    viewModel.updateTodos();

    viewModel.filterProperty().set(TodoFilter.COMPLETED);

    assertAll(
        () -> assertTrue(viewModel.todosAvailableProperty().get(), "todos available"),
        () -> assertEquals(1, viewModel.activeTodoCountProperty().get(), "active todo count"),
        () -> assertFalse(viewModel.allCompletedProperty().get(), "all completed"),
        () -> assertFalse(viewModel.allActiveProperty().get(), "all active"),
        () -> assertEquals(TodoFilter.COMPLETED, viewModel.filterProperty().get(), "filter"),
        () -> assertEquals("", viewModel.newTodoProperty().get(), "new todo"),
        () ->
            assertEquals(
                List.of(new Todo("1", "Taste JavaScript", true)),
                viewModel.getFilteredTodos(),
                "filtered todos"));
  }

  @Test
  void newTodo() {
    var viewModel = new MainViewModel();
    viewModel.initMessageHandling(
        new TestingMessageHandling(
            new Todo("1", "Taste JavaScript", true), new Todo("2", "Buy a unicorn", false)));
    viewModel.updateTodos();

    viewModel.newTodoProperty().set("Lorem ipsum");
    viewModel.newTodo();

    assertAll(
        () -> assertTrue(viewModel.todosAvailableProperty().get(), "todos available"),
        () -> assertEquals(2, viewModel.activeTodoCountProperty().get(), "active todo count"),
        () -> assertFalse(viewModel.allCompletedProperty().get(), "all completed"),
        () -> assertFalse(viewModel.allActiveProperty().get(), "all active"),
        () -> assertEquals(TodoFilter.ALL, viewModel.filterProperty().get(), "filter"),
        () -> assertEquals("", viewModel.newTodoProperty().get(), "new todo"),
        () ->
            assertEquals(
                List.of(
                    new Todo("1", "Taste JavaScript", true),
                    new Todo("2", "Buy a unicorn", false),
                    new Todo("3", "Lorem ipsum", false)),
                viewModel.getFilteredTodos(),
                "filtered todos"));
  }

  @Test
  void newTodoWithBlankTitle() {
    var viewModel = new MainViewModel();
    viewModel.initMessageHandling(
        new TestingMessageHandling(
            new Todo("1", "Taste JavaScript", true), new Todo("2", "Buy a unicorn", false)));
    viewModel.updateTodos();

    viewModel.newTodoProperty().set("");
    viewModel.newTodo();

    assertAll(
        () -> assertTrue(viewModel.todosAvailableProperty().get(), "todos available"),
        () -> assertEquals(1, viewModel.activeTodoCountProperty().get(), "active todo count"),
        () -> assertFalse(viewModel.allCompletedProperty().get(), "all completed"),
        () -> assertFalse(viewModel.allActiveProperty().get(), "all active"),
        () -> assertEquals(TodoFilter.ALL, viewModel.filterProperty().get(), "filter"),
        () -> assertEquals("", viewModel.newTodoProperty().get(), "new todo"),
        () ->
            assertEquals(
                List.of(
                    new Todo("1", "Taste JavaScript", true), new Todo("2", "Buy a unicorn", false)),
                viewModel.getFilteredTodos(),
                "filtered todos"));
  }

  private static class TestingMessageHandling implements MessageHandling {
    private final List<Todo> todos = new ArrayList<>();

    TestingMessageHandling(Todo... todos) {
      this.todos.addAll(List.of(todos));
    }

    @Override
    public CommandStatus handle(ClearCompletedCommand command) {
      return new Success();
    }

    @Override
    public CommandStatus handle(DestroyCommand command) {
      return new Success();
    }

    @Override
    public CommandStatus handle(EditCommand command) {
      return new Success();
    }

    @Override
    public CommandStatus handle(NewTodoCommand command) {
      todos.add(new Todo(String.valueOf(todos.size() + 1), command.getTitle(), false));
      return new Success();
    }

    @Override
    public CommandStatus handle(ToggleAllCommand command) {
      return new Success();
    }

    @Override
    public CommandStatus handle(ToggleCommand command) {
      return new Success();
    }

    @Override
    public TodosQueryResult handle(TodosQuery query) {
      return new TodosQueryResult(todos);
    }
  }
}
