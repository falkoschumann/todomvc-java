/*
 * TodoMVC - Frontend
 * Copyright (c) 2021 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.frontend;

import de.muspellheim.todomvc.contract.data.Todo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

public class MainView extends VBox {
  private final MainViewModel viewModel = new MainViewModel();

  @FXML private HBox commandBar;
  @FXML private TextFlow todoCount;
  @FXML private ChoiceBox<TodoFilter> filter;
  @FXML private ToggleButton toggleAll;
  @FXML private Button clearCompleted;
  @FXML private TextField newTodo;
  @FXML private ListView<Todo> todoList;

  @FXML
  private void initialize() {
    commandBar.visibleProperty().bind(viewModel.todosAvailableProperty());
    commandBar.managedProperty().bind(viewModel.todosAvailableProperty());

    filter.setConverter(new TodoFilterStringConverter());
    filter.getItems().setAll(TodoFilter.values());
    filter.valueProperty().bindBidirectional(viewModel.filterProperty());

    newTodo.textProperty().bindBidirectional(viewModel.newTodoProperty());

    todoList.setCellFactory(this::createTodoListCell);
    todoList.visibleProperty().bind(viewModel.todosAvailableProperty());
    todoList.managedProperty().bind(todoList.visibleProperty());
  }

  private ListCell<Todo> createTodoListCell(ListView<Todo> view) {
    var cell = new TodoListCell2();
    cell.setOnToggle(viewModel::toggle);
    cell.setOnEdit(viewModel::edit);
    cell.setOnDestroy(viewModel::destroy);
    return cell;
  }

  public void run() {
    viewModel.updateTodos();
  }

  @FXML
  private void handleOpenInfo() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @FXML
  private void handleToggleAll() {
    viewModel.toggleAll();
  }

  @FXML
  private void handleNewTodo() {
    viewModel.newTodo();
  }

  @FXML
  private void handleClearCompleted() {
    viewModel.clearCompleted();
  }
}
