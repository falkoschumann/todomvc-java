/*
 * TodoMVC - Backend Server
 * Copyright (c) 2021 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.server;

import de.muspellheim.todomvc.backend.TodoRepository;
import de.muspellheim.todomvc.backend.adapters.JsonTodoRepository;
import de.muspellheim.todomvc.backend.adapters.MemoryTodoRepository;
import de.muspellheim.todomvc.contract.data.Todo;
import io.quarkus.runtime.configuration.ProfileManager;
import java.nio.file.Paths;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class TodoMvcProducer {
  @Produces
  TodoRepository getTodoRepository() {
    var profile = ProfileManager.getActiveProfile();
    if (profile.equals("test")) {
      var repository = new MemoryTodoRepository();
      repository.store(
          List.of(
              new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
              new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)));
      return repository;
    } else {
      // TODO File konfigurierbar machen
      var file = Paths.get("todos.json");
      return new JsonTodoRepository(file);
    }
  }
}
