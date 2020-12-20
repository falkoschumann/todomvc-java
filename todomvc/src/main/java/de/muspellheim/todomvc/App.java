/*
 * TodoMVC - Application
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc;

import de.muspellheim.todomvc.backend.adapters.TodoRepositoryJson;
import de.muspellheim.todomvc.backend.messagehandlers.ClearCompletedCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.DestroyCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.EditCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.NewTodoCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.TodosQueryHandler;
import de.muspellheim.todomvc.backend.messagehandlers.ToggleAllCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.ToggleCommandHandler;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import de.muspellheim.todomvc.frontend.TodoAppViewController;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage stage) {
    TodoRepositoryJson repository = createRepository();

    var newTodoCommandHandler = new NewTodoCommandHandler(repository);
    var toggleCommandHandler = new ToggleCommandHandler(repository);
    var toggleAllCommandHandler = new ToggleAllCommandHandler(repository);
    var editCommandHandler = new EditCommandHandler(repository);
    var destroyCommandHandler = new DestroyCommandHandler(repository);
    var clearCompletedCommandHandler = new ClearCompletedCommandHandler(repository);
    var todosQueryHandler = new TodosQueryHandler(repository);

    var root = TodoAppViewController.load();
    var controller = root.getValue();
    controller.setOnNewTodoCommand(
        it -> {
          newTodoCommandHandler.handle(it);
          var result = todosQueryHandler.handle(new TodosQuery());
          controller.display(result);
        });
    controller.setOnToggleAllCommand(
        it -> {
          toggleAllCommandHandler.handle(it);
          var result = todosQueryHandler.handle(new TodosQuery());
          controller.display(result);
        });
    controller.setOnToggleCommand(
        it -> {
          toggleCommandHandler.handle(it);
          var result = todosQueryHandler.handle(new TodosQuery());
          controller.display(result);
        });
    controller.setOnDestroyCommand(
        it -> {
          destroyCommandHandler.handle(it);
          var result = todosQueryHandler.handle(new TodosQuery());
          controller.display(result);
        });
    controller.setOnEditCommand(
        it -> {
          editCommandHandler.handle(it);
          var result = todosQueryHandler.handle(new TodosQuery());
          controller.display(result);
        });
    controller.setOnClearCompletedCommand(
        it -> {
          clearCompletedCommandHandler.handle(it);
          var result = todosQueryHandler.handle(new TodosQuery());
          controller.display(result);
        });
    controller.setOnTodoListQuery(
        it -> {
          var result = todosQueryHandler.handle(new TodosQuery());
          controller.display(result);
        });

    var result = todosQueryHandler.handle(new TodosQuery());
    controller.display(result);

    var view = root.getKey();
    Scene scene = new Scene(view);
    stage.setScene(scene);
    stage.setTitle("TodoMVC");
    stage.show();
  }

  protected TodoRepositoryJson createRepository() {
    var file = Paths.get("todos.json");
    return new TodoRepositoryJson(file);
  }
}
