/*
 * TodoMVC - Frontend
 * Copyright (c) 2021 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.frontend;

import de.muspellheim.todomvc.contract.MessageHandling;
import de.muspellheim.todomvc.contract.data.Todo;
import java.net.URL;
import java.util.Properties;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class MainView extends VBox {
  @FXML private HBox commandBar;
  @FXML private TextFlow todoCount;
  @FXML private ChoiceBox<TodoFilter> filter;
  @FXML private ToggleButton toggleAll;
  @FXML private Button clearCompleted;
  @FXML private TextField newTodo;
  @FXML private ListView<Todo> todoList;

  private final MainViewModel viewModel = new MainViewModel();
  private URL appIconUrl;
  private Properties appProperties;

  public static MainView create(
      Stage stage, MessageHandling messageHandling, URL appIconUrl, Properties appProperties) {
    var factory = new ViewControllerFactory(MainView.class);

    var scene = new Scene(factory.getView());
    stage.setScene(scene);
    stage.setTitle("Todos");
    stage.setMinWidth(320);
    stage.setMinHeight(569);

    MainView controller = factory.getController();
    controller.viewModel.initMessageHandling(messageHandling);
    controller.appIconUrl = appIconUrl;
    controller.appProperties = appProperties;
    return controller;
  }

  @FXML
  private void initialize() {
    commandBar.visibleProperty().bind(viewModel.todosAvailableProperty());
    commandBar.managedProperty().bind(viewModel.todosAvailableProperty());

    viewModel.activeTodoCountProperty().addListener(this::updateTodoCount);

    filter.setConverter(new TodoFilterStringConverter());
    filter.getItems().setAll(TodoFilter.values());
    filter.valueProperty().bindBidirectional(viewModel.filterProperty());

    toggleAll.selectedProperty().bindBidirectional(viewModel.allCompletedProperty());
    clearCompleted.disableProperty().bind(viewModel.allActiveProperty());

    newTodo.textProperty().bindBidirectional(viewModel.newTodoProperty());

    todoList.setItems(viewModel.getFilteredTodos());
    todoList.setCellFactory(this::createTodoListCell);
    todoList.visibleProperty().bind(viewModel.todosAvailableProperty());
    todoList.managedProperty().bind(todoList.visibleProperty());
  }

  private void updateTodoCount(Observable observable) {
    var activeTodoCount = viewModel.activeTodoCountProperty().get();
    var text = new Text(String.valueOf(activeTodoCount));
    text.setStyle("-fx-font-weight: bold");
    todoCount
        .getChildren()
        .setAll(text, new Text(" item" + (activeTodoCount == 1 ? "" : "s") + " left"));
  }

  private ListCell<Todo> createTodoListCell(ListView<Todo> view) {
    var cell = new TodoListCell();
    cell.setOnToggle(viewModel::toggle);
    cell.setOnEdit(viewModel::edit);
    cell.setOnDestroy(viewModel::destroy);
    return cell;
  }

  public void run() {
    getWindow().show();
    viewModel.updateTodos();
  }

  private Stage getWindow() {
    return (Stage) commandBar.getScene().getWindow();
  }

  @FXML
  private void handleOpenInfo() {
    var stage = new Stage();
    stage.initOwner(getWindow());
    var infoView = InfoView.create(stage, appIconUrl, appProperties);
    infoView.run();
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
