/*
 * TodoMVC - Frontend
 * Copyright (c) 2021 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.frontend;

import de.muspellheim.todomvc.contract.MessageHandling;
import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleAllCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainViewModel {
  private final ReadOnlyBooleanWrapper todosAvailable = new ReadOnlyBooleanWrapper(false);
  private final ReadOnlyBooleanWrapper allCompleted = new ReadOnlyBooleanWrapper(false);
  private final ObjectProperty<TodoFilter> filter = new SimpleObjectProperty<>(TodoFilter.ALL);
  private final StringProperty newTodo = new SimpleStringProperty("");
  private final ObservableList<Todo> filteredTodos = FXCollections.observableArrayList();

  private MessageHandling messageHandling;
  private List<Todo> todos = List.of();

  public ReadOnlyBooleanProperty todosAvailableProperty() {
    return todosAvailable.getReadOnlyProperty();
  }

  public ReadOnlyBooleanProperty allCompletedProperty() {
    return allCompleted.getReadOnlyProperty();
  }

  public ObjectProperty<TodoFilter> filterProperty() {
    return filter;
  }

  public StringProperty newTodoProperty() {
    return newTodo;
  }

  public ObservableList<Todo> getFilteredTodos() {
    return filteredTodos;
  }

  public void updateTodos() {
    var result = messageHandling.handle(new TodosQuery());
    todos = result.getTodos();

    updateFilteredTodos();

    // TODO Übernimm von MainViewController::display(TodosQueryResult)
  }

  private void updateFilteredTodos() {
    var filtered =
        todos.stream()
            .filter(
                it ->
                    filter.get() == TodoFilter.ACTIVE && it.isActive()
                        || filter.get() == TodoFilter.COMPLETED && it.isCompleted()
                        || filter.get() == TodoFilter.ALL)
            .collect(Collectors.toList());
    filteredTodos.setAll(filtered);
  }

  public void newTodo() {
    var text = newTodo.get();
    if (text.isBlank()) {
      return;
    }

    messageHandling.handle(new NewTodoCommand(text));
    newTodo.set("");
    updateTodos();
  }

  public void toggle(Todo todo) {
    messageHandling.handle(new ToggleCommand(todo.getId()));
    updateTodos();
  }

  public void edit(Todo todo) {
    messageHandling.handle(new EditCommand(todo.getId(), todo.getTitle()));
    updateTodos();
  }

  public void destroy(Todo todo) {
    messageHandling.handle(new DestroyCommand(todo.getId()));
    updateTodos();
  }

  public void clearCompleted() {
    messageHandling.handle(new ClearCompletedCommand());
    updateTodos();
  }

  public void toggleAll() {
    messageHandling.handle(new ToggleAllCommand(!allCompleted.get()));
    updateTodos();
  }
}
