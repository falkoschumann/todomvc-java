/*
 * TodoMVC - Backend Server
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.server;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.gson.Gson;
import de.muspellheim.todomvc.backend.TodoRepository;
import de.muspellheim.todomvc.backend.adapters.MemoryTodoRepository;
import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.contract.messages.Command;
import de.muspellheim.todomvc.contract.messages.Failure;
import de.muspellheim.todomvc.contract.messages.HttpCommandStatus;
import de.muspellheim.todomvc.contract.messages.Success;
import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleAllCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import de.muspellheim.todomvc.contract.messages.queries.TodosQueryResult;
import io.quarkus.test.junit.QuarkusTest;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
class TodoMvcControllerTests {

  private TodoRepository repository;

  @BeforeEach
  void setUp() throws Exception {
    repository = new MemoryTodoRepository();
    repository.store(createTodos());
    TodoMvcController.repository = repository;
  }

  @Test
  void handleNewTodoCommandWithSuccess() throws Exception {
    var command = new NewTodoCommand("Foobar");
    HttpResponse<String> response = sendCommand("newtodocommand", command);
    var status = new Gson().fromJson(response.body(), HttpCommandStatus.class);

    assertAll(
        "Handle new todo command with success",
        () -> assertEquals(200, response.statusCode(), "HTTP status is OK"),
        () ->
            assertEquals(new HttpCommandStatus(new Success()), status, "Command status is success"),
        () -> assertEquals(3, repository.load().size(), "Todo added to list"),
        () -> assertNotNull(repository.load().get(2).getId(), "New todo id is set"),
        () -> assertEquals("Foobar", repository.load().get(2).getTitle(), "New todo title is set"),
        () -> assertFalse(repository.load().get(2).isCompleted(), "New todo is not completed"));
  }

  @Test
  void handleNewTodoCommandMissingTitleWithFailure() throws Exception {
    var command = new EmptyCommand();
    HttpResponse<String> response = sendCommand("newtodocommand", command);
    var status = new Gson().fromJson(response.body(), HttpCommandStatus.class);

    assertAll(
        "Handle new todo command missing title with failure",
        () -> assertEquals(400, response.statusCode(), "HTTP status is BAD REQUEST"),
        () ->
            assertEquals(
                new HttpCommandStatus(new Failure("Missing property `title` in new todo command.")),
                status,
                "Command status is failure"),
        () -> assertEquals(createTodos(), repository.load(), "Todo list unchanged"));
  }

  @Test
  void handleToggleCommandWithSuccess() throws Exception {
    var command = new ToggleCommand("d2f7760d-8f03-4cb3-9176-06311cb89993");
    HttpResponse<String> response = sendCommand("togglecommand", command);
    var status = new Gson().fromJson(response.body(), HttpCommandStatus.class);

    assertAll(
        "Handle toggle command with success",
        () -> assertEquals(200, response.statusCode(), "HTTP status is OK"),
        () ->
            assertEquals(new HttpCommandStatus(new Success()), status, "Command status is success"),
        () ->
            assertEquals(
                List.of(
                    new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
                    new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", true)),
                repository.load(),
                "Todo in list updated"));
  }

  @Test
  void handleToggleCommand_MissingId_Error() throws Exception {
    var command = new EmptyCommand();
    HttpResponse<String> response = sendCommand("togglecommand", command);
    var status = new Gson().fromJson(response.body(), HttpCommandStatus.class);

    assertAll(
        "Handle toggle command missing id with failure",
        () -> assertEquals(400, response.statusCode(), "HTTP status is BAD REQUEST"),
        () ->
            assertEquals(
                new HttpCommandStatus(new Failure("Missing property `id` in toggle command.")),
                status,
                "Command status is failure"),
        () -> assertEquals(createTodos(), repository.load(), "Todo list unchanged"));
  }

  @Test
  void handleToggleAllCommandWithSuccess() throws Exception {
    var command = new ToggleAllCommand(true);
    HttpResponse<String> response = sendCommand("toggleallcommand", command);
    var status = new Gson().fromJson(response.body(), HttpCommandStatus.class);

    assertAll(
        "Handle toggle all command with success",
        () -> assertEquals(200, response.statusCode(), "HTTP status is OK"),
        () ->
            assertEquals(new HttpCommandStatus(new Success()), status, "Command status is success"),
        () ->
            assertEquals(
                List.of(
                    new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
                    new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", true)),
                repository.load(),
                "Todos in list updated"));
  }

  @Test
  void handleToggleAllCommandMissingCompletedWithFailure() throws Exception {
    var command = new EmptyCommand();
    HttpResponse<String> response = sendCommand("toggleallcommand", command);
    var status = new Gson().fromJson(response.body(), HttpCommandStatus.class);

    assertAll(
        "Handle toggle all command missing completed with failure",
        () -> assertEquals(400, response.statusCode(), "HTTP status is BAD REQUEST"),
        () ->
            assertEquals(
                new HttpCommandStatus(
                    new Failure("Missing property `completed` in toggle all command.")),
                status,
                "Command status is failure"),
        () -> assertEquals(createTodos(), repository.load(), "Todo list unchanged"));
  }

  @Test
  void handleEditCommandWithSuccess() throws Exception {
    var command = new EditCommand("d2f7760d-8f03-4cb3-9176-06311cb89993", "Foobar");
    HttpResponse<String> response = sendCommand("editcommand", command);
    var status = new Gson().fromJson(response.body(), HttpCommandStatus.class);

    assertAll(
        "Handle edit command with success",
        () -> assertEquals(200, response.statusCode(), "HTTP status is OK"),
        () ->
            assertEquals(new HttpCommandStatus(new Success()), status, "Command status is success"),
        () ->
            assertEquals(
                List.of(
                    new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
                    new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Foobar", false)),
                repository.load(),
                "Todo in list updated"));
  }

  @Test
  void handleEditCommandMissingIdWithFailure() throws Exception {
    var command = new EditCommand(null, "Foobar");
    HttpResponse<String> response = sendCommand("editcommand", command);
    var status = new Gson().fromJson(response.body(), HttpCommandStatus.class);

    assertAll(
        "Handle edit command missing id with failure",
        () -> assertEquals(400, response.statusCode(), "HTTP status is BAD REQUEST"),
        () ->
            assertEquals(
                new HttpCommandStatus(new Failure("Missing property `id` in edit command.")),
                status,
                "Command status is failure"),
        () -> assertEquals(createTodos(), repository.load(), "Todo list unchanged"));
  }

  @Test
  void handleEditCommandMissingTitleWithFailure() throws Exception {
    var command = new EditCommand("d2f7760d-8f03-4cb3-9176-06311cb89993", null);
    HttpResponse<String> response = sendCommand("editcommand", command);
    var status = new Gson().fromJson(response.body(), HttpCommandStatus.class);

    assertAll(
        "Handle edit command missing title with failure",
        () -> assertEquals(400, response.statusCode(), "HTTP status is BAD REQUEST"),
        () ->
            assertEquals(
                new HttpCommandStatus(new Failure("Missing property `title` in edit command.")),
                status,
                "Command status is failure"),
        () -> assertEquals(createTodos(), repository.load(), "Todo list unchanged"));
  }

  @Test
  void handleDestroyCommandWithSuccess() throws Exception {
    var command = new DestroyCommand("119e6785-8ffc-42e0-8df6-dbc64881f2b7");
    HttpResponse<String> response = sendCommand("destroycommand", command);
    var status = new Gson().fromJson(response.body(), HttpCommandStatus.class);

    assertAll(
        "Handle destroy command with success",
        () -> assertEquals(200, response.statusCode(), "HTTP status is OK"),
        () ->
            assertEquals(new HttpCommandStatus(new Success()), status, "Command status is success"),
        () ->
            assertEquals(
                List.of(new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)),
                repository.load(),
                "Todo removed from list"));
  }

  @Test
  void handleDestroyCommandMissingIdWithFailure() throws Exception {
    var command = new EmptyCommand();
    HttpResponse<String> response = sendCommand("destroycommand", command);
    var status = new Gson().fromJson(response.body(), HttpCommandStatus.class);

    assertAll(
        "Handle destroy command missing id with failure",
        () -> assertEquals(400, response.statusCode(), "HTTP status is BAD REQUEST"),
        () ->
            assertEquals(
                new HttpCommandStatus(new Failure("Missing property `id` in edit command.")),
                status,
                "Command status is failure"),
        () -> assertEquals(createTodos(), repository.load(), "Todo list unchanged"));
  }

  @Test
  void handleClearCompletedCommandWithSuccess() throws Exception {
    var command = new ClearCompletedCommand();
    HttpResponse<String> response = sendCommand("clearcompletedcommand", command);
    var status = new Gson().fromJson(response.body(), HttpCommandStatus.class);

    assertAll(
        "Handle clear completed command with success",
        () -> assertEquals(200, response.statusCode(), "HTTP status is OK"),
        () ->
            assertEquals(new HttpCommandStatus(new Success()), status, "Command status is success"),
        () ->
            assertEquals(
                List.of(new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)),
                repository.load(),
                "Todo list updated"));
  }

  private static HttpResponse<String> sendCommand(String path, Command command) throws Exception {
    var client = HttpClient.newHttpClient();

    var body = new Gson().toJson(command);
    var request =
        HttpRequest.newBuilder(URI.create("http://localhost:8081/api/" + path))
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(body))
            .build();
    return client.send(request, HttpResponse.BodyHandlers.ofString());
  }

  @Test
  void handleTodosQuery() throws Exception {
    var client = HttpClient.newHttpClient();

    var request =
        HttpRequest.newBuilder(URI.create("http://localhost:8081/api/todosquery"))
            .header("Accept", "application/json")
            .GET()
            .build();
    var response = client.send(request, HttpResponse.BodyHandlers.ofString());
    var result = new Gson().fromJson(response.body(), TodosQueryResult.class);

    assertEquals(new TodosQueryResult(createTodos()), result);
  }

  private static List<Todo> createTodos() {
    return List.of(
        new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
        new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false));
  }
}
