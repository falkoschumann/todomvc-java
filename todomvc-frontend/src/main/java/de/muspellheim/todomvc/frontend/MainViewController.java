/*
 * TodoMVC - Frontend
 * Copyright (c) 2021 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.frontend;

import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleAllCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import de.muspellheim.todomvc.contract.messages.queries.TodosQueryResult;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public class MainViewController {
  @Getter @Setter private Runnable onOpenInfo;
  @Getter @Setter private Consumer<NewTodoCommand> onNewTodoCommand;
  @Getter @Setter private Consumer<ToggleAllCommand> onToggleAllCommand;
  @Getter @Setter private Consumer<ToggleCommand> onToggleCommand;
  @Getter @Setter private Consumer<EditCommand> onEditCommand;
  @Getter @Setter private Consumer<DestroyCommand> onDestroyCommand;
  @Getter @Setter private Consumer<ClearCompletedCommand> onClearCompletedCommand;
  @Getter @Setter private Consumer<TodosQuery> onTodosQuery;

  @FXML private HBox commandBar;
  @FXML private TextFlow todoCount;
  @FXML private ChoiceBox<TodoFilter> filter;
  @FXML private ToggleButton toggleAll;
  @FXML private Button clearCompleted;
  @FXML private TextField newTodo;
  @FXML private ListView<Todo> todoList;

  private final ReadOnlyBooleanWrapper todosAvailable = new ReadOnlyBooleanWrapper(false);
  private List<Todo> todos = List.of();

  public static MainViewController create(Stage stage) {
    var factory = new ViewControllerFactory(MainViewController.class);

    var scene = new Scene(factory.getView());
    stage.setScene(scene);
    stage.setTitle("Todos");
    stage.setMinWidth(320);
    stage.setMinHeight(569);

    return factory.getController();
  }

  private Stage getWindow() {
    return (Stage) commandBar.getScene().getWindow();
  }

  public void run() {
    getWindow().show();
    onTodosQuery.accept(new TodosQuery());
  }

  public void display(TodosQueryResult result) {
    todos = result.getTodos();
    updateTodoList();

    var completedCount = result.getTodos().stream().filter(Todo::isCompleted).count();
    var activeTodoCount = result.getTodos().stream().filter(Todo::isActive).count();

    todosAvailable.set(!result.getTodos().isEmpty());

    boolean allCompleted = result.getTodos().size() == completedCount;
    toggleAll.setSelected(todosAvailable.get() && allCompleted);

    var text = new Text(String.valueOf(activeTodoCount));
    text.setStyle("-fx-font-weight: bold");
    todoCount
        .getChildren()
        .setAll(text, new Text(" item" + (completedCount == 1 ? "" : "s") + " left"));

    clearCompleted.setDisable(completedCount == 0);
  }

  @FXML
  private void initialize() {
    //
    // Build
    //

    filter.getItems().setAll(TodoFilter.values());
    filter.setValue(TodoFilter.ALL);
    filter.setConverter(new TodoFilterStringConverter());

    todoList.setCellFactory(
        view -> {
          var cell = new TodoListCell();
          cell.setOnToggleCommand(onToggleCommand);
          cell.setOnEditCommand(onEditCommand);
          cell.setOnDestroyCommand(onDestroyCommand);
          return cell;
        });

    //
    // Bind
    //

    commandBar.visibleProperty().bind(todosAvailable);
    commandBar.managedProperty().bind(commandBar.visibleProperty());
    todoList.visibleProperty().bind(todosAvailable);
    todoList.managedProperty().bind(todoList.visibleProperty());

    filter.valueProperty().addListener(o -> updateTodoList());
  }

  @FXML
  private void handleOpenInfo() {
    onOpenInfo.run();
  }

  @FXML
  private void handleToggleAll() {
    var checked = toggleAll.isSelected();
    onToggleAllCommand.accept(new ToggleAllCommand(checked));
  }

  @FXML
  private void handleNewTodo() {
    var text = newTodo.getText();
    if (text.isBlank()) {
      return;
    }

    onNewTodoCommand.accept(new NewTodoCommand(text));
    newTodo.setText("");
  }

  @FXML
  private void handleClearCompleted() {
    onClearCompletedCommand.accept(new ClearCompletedCommand());
  }

  private void updateTodoList() {
    var filteredTodos =
        todos.stream()
            .filter(
                it ->
                    filter.getValue() == TodoFilter.ACTIVE && it.isActive()
                        || filter.getValue() == TodoFilter.COMPLETED && it.isCompleted()
                        || filter.getValue() == TodoFilter.ALL)
            .collect(Collectors.toList());
    todoList.getItems().setAll(filteredTodos);
  }
}
