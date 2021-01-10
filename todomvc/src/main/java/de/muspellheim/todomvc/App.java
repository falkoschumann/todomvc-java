/*
 * TodoMVC - Application
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc;

import de.muspellheim.todomvc.backend.TodoRepository;
import de.muspellheim.todomvc.backend.adapters.JsonTodoRepository;
import de.muspellheim.todomvc.backend.adapters.MemoryTodoRepository;
import de.muspellheim.todomvc.backend.messagehandlers.ClearCompletedCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.DestroyCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.EditCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.NewTodoCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.TodosQueryHandler;
import de.muspellheim.todomvc.backend.messagehandlers.ToggleAllCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.ToggleCommandHandler;
import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import de.muspellheim.todomvc.frontend.AboutViewController;
import de.muspellheim.todomvc.frontend.TodosViewController;
import java.nio.file.Paths;
import java.util.List;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
  private TodoRepository repository;
  private boolean useSystemMenuBar = true;

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void init() throws Exception {
    var demo = getParameters().getUnnamed().contains("--demo");
    if (demo) {
      System.out.println("Run in demo mode...");
      repository = new MemoryTodoRepository();
      repository.store(
          List.of(
              new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
              new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)));
    } else {
      var file = Paths.get("todos.json");
      repository = new JsonTodoRepository(file);
    }

    if (getParameters().getUnnamed().contains("--noSystemMenuBar")) {
      useSystemMenuBar = false;
    }
  }

  @Override
  public void start(Stage primaryStage) {
    //
    // Build
    //

    var newTodoCommandHandler = new NewTodoCommandHandler(repository);
    var toggleCommandHandler = new ToggleCommandHandler(repository);
    var toggleAllCommandHandler = new ToggleAllCommandHandler(repository);
    var editCommandHandler = new EditCommandHandler(repository);
    var destroyCommandHandler = new DestroyCommandHandler(repository);
    var clearCompletedCommandHandler = new ClearCompletedCommandHandler(repository);
    var todosQueryHandler = new TodosQueryHandler(repository);

    var todosViewController = TodosViewController.create(primaryStage, useSystemMenuBar);

    var aboutStage = new Stage();
    aboutStage.initOwner(primaryStage);
    var aboutViewController = AboutViewController.create(aboutStage);
    aboutViewController.initVersion(System.getProperty("app.version"));
    aboutViewController.initCopyright(System.getProperty("app.copyright"));

    //
    // Bind
    //

    todosViewController.setOnOpenAbout(() -> aboutStage.show());
    todosViewController.setOnNewTodoCommand(
        it -> {
          newTodoCommandHandler.handle(it);
          var result = todosQueryHandler.handle(new TodosQuery());
          todosViewController.display(result);
        });
    todosViewController.setOnToggleAllCommand(
        it -> {
          toggleAllCommandHandler.handle(it);
          var result = todosQueryHandler.handle(new TodosQuery());
          todosViewController.display(result);
        });
    todosViewController.setOnToggleCommand(
        it -> {
          toggleCommandHandler.handle(it);
          var result = todosQueryHandler.handle(new TodosQuery());
          todosViewController.display(result);
        });
    todosViewController.setOnDestroyCommand(
        it -> {
          destroyCommandHandler.handle(it);
          var result = todosQueryHandler.handle(new TodosQuery());
          todosViewController.display(result);
        });
    todosViewController.setOnEditCommand(
        it -> {
          editCommandHandler.handle(it);
          var result = todosQueryHandler.handle(new TodosQuery());
          todosViewController.display(result);
        });
    todosViewController.setOnClearCompletedCommand(
        it -> {
          clearCompletedCommandHandler.handle(it);
          var result = todosQueryHandler.handle(new TodosQuery());
          todosViewController.display(result);
        });
    todosViewController.setOnTodosQuery(
        it -> {
          var result = todosQueryHandler.handle(new TodosQuery());
          todosViewController.display(result);
        });

    //
    // Run
    //

    todosViewController.run();
  }
}
