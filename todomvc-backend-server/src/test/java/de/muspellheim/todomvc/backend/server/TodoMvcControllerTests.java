/*
 * TodoMVC - Backend Server
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.server;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleAllCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import javax.ws.rs.core.Response.Status;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
class TodoMvcControllerTests {
  @Test
  @Order(1)
  void handleTodosQuery() {
    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new TodosQuery())
        .post("/api/todos-query")
        .then()
        .statusCode(Status.OK.getStatusCode())
        .contentType(is(ContentType.JSON.toString()))
        .body("todos", hasSize(2))
        .body("todos[0].id", is("119e6785-8ffc-42e0-8df6-dbc64881f2b7"))
        .body("todos[0].title", is("Taste JavaScript"))
        .body("todos[0].completed", is(true))
        .body("todos[1].id", is("d2f7760d-8f03-4cb3-9176-06311cb89993"))
        .body("todos[1].title", is("Buy a unicorn"))
        .body("todos[1].completed", is(false));
  }

  @Test
  @Order(2)
  void handleNewTodoCommandWithSuccess() {
    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new NewTodoCommand("Foobar"))
        .post("/api/new-todo-command")
        .then()
        .statusCode(Status.OK.getStatusCode())
        .body("success", is(true));

    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new TodosQuery())
        .post("/api/todos-query")
        .then()
        .statusCode(Status.OK.getStatusCode())
        .contentType(is(ContentType.JSON.toString()))
        .body("todos", hasSize(3))
        .body("todos[0].id", is("119e6785-8ffc-42e0-8df6-dbc64881f2b7"))
        .body("todos[0].title", is("Taste JavaScript"))
        .body("todos[0].completed", is(true))
        .body("todos[1].id", is("d2f7760d-8f03-4cb3-9176-06311cb89993"))
        .body("todos[1].title", is("Buy a unicorn"))
        .body("todos[1].completed", is(false))
        .body("todos[2].id", is(any(String.class)))
        .body("todos[2].title", is("Foobar"))
        .body("todos[2].completed", is(false));
  }

  @Test
  void handleNewTodoCommandMissingTitleWithFailure() {
    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new EmptyCommand())
        .post("/api/new-todo-command")
        .then()
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body("success", is(false))
        .body("errorMessage", is("Missing property `title` in new todo command."));
  }

  @Test
  void handleNewTodoCommandEmptyTitleWithFailure() {
    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new NewTodoCommand(""))
        .post("/api/new-todo-command")
        .then()
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body("success", is(false))
        .body("errorMessage", is("Property `title` is empty in new todo command."));
  }

  @Test
  @Order(3)
  void handleToggleCommandWithSuccess() {
    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new ToggleCommand("d2f7760d-8f03-4cb3-9176-06311cb89993"))
        .post("/api/toggle-command")
        .then()
        .statusCode(Status.OK.getStatusCode())
        .body("success", is(true));

    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new TodosQuery())
        .post("/api/todos-query")
        .then()
        .statusCode(Status.OK.getStatusCode())
        .contentType(is(ContentType.JSON.toString()))
        .body("todos", hasSize(3))
        .body("todos[0].id", is("119e6785-8ffc-42e0-8df6-dbc64881f2b7"))
        .body("todos[0].title", is("Taste JavaScript"))
        .body("todos[0].completed", is(true))
        .body("todos[1].id", is("d2f7760d-8f03-4cb3-9176-06311cb89993"))
        .body("todos[1].title", is("Buy a unicorn"))
        .body("todos[1].completed", is(true))
        .body("todos[2].id", is(any(String.class)))
        .body("todos[2].title", is("Foobar"))
        .body("todos[2].completed", is(false));
  }

  @Test
  void handleToggleCommandMissingIdWithFailure() {
    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new EmptyCommand())
        .post("/api/toggle-command")
        .then()
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body("success", is(false))
        .body("errorMessage", is("Missing property `id` in toggle command."));
  }

  @Test
  void handleToggleCommandEmptyIdWithFailure() {
    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new ToggleCommand(""))
        .post("/api/toggle-command")
        .then()
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body("success", is(false))
        .body("errorMessage", is("Property `id` is empty in toggle command."));
  }

  @Test
  @Order(4)
  void handleToggleAllCommandWithSuccess() {
    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new ToggleAllCommand(true))
        .post("/api/toggle-all-command")
        .then()
        .statusCode(Status.OK.getStatusCode())
        .body("success", is(true));

    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new TodosQuery())
        .post("/api/todos-query")
        .then()
        .statusCode(Status.OK.getStatusCode())
        .contentType(is(ContentType.JSON.toString()))
        .body("todos", hasSize(3))
        .body("todos[0].id", is("119e6785-8ffc-42e0-8df6-dbc64881f2b7"))
        .body("todos[0].title", is("Taste JavaScript"))
        .body("todos[0].completed", is(true))
        .body("todos[1].id", is("d2f7760d-8f03-4cb3-9176-06311cb89993"))
        .body("todos[1].title", is("Buy a unicorn"))
        .body("todos[1].completed", is(true))
        .body("todos[2].id", is(any(String.class)))
        .body("todos[2].title", is("Foobar"))
        .body("todos[2].completed", is(true));
  }

  @Test
  void handleToggleAllCommandMissingCompletedWithFailure() {
    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new EmptyCommand())
        .post("/api/toggle-all-command")
        .then()
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body("success", is(false))
        .body("errorMessage", is("Missing property `completed` in toggle all command."));
  }

  @Test
  @Order(5)
  void handleEditCommandWithSuccess() {
    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new EditCommand("d2f7760d-8f03-4cb3-9176-06311cb89993", "Foobar"))
        .post("/api/edit-command")
        .then()
        .statusCode(Status.OK.getStatusCode())
        .body("success", is(true));

    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new TodosQuery())
        .post("/api/todos-query")
        .then()
        .statusCode(Status.OK.getStatusCode())
        .contentType(is(ContentType.JSON.toString()))
        .body("todos", hasSize(3))
        .body("todos[0].id", is("119e6785-8ffc-42e0-8df6-dbc64881f2b7"))
        .body("todos[0].title", is("Taste JavaScript"))
        .body("todos[0].completed", is(true))
        .body("todos[1].id", is("d2f7760d-8f03-4cb3-9176-06311cb89993"))
        .body("todos[1].title", is("Foobar"))
        .body("todos[1].completed", is(true))
        .body("todos[2].id", is(any(String.class)))
        .body("todos[2].title", is("Foobar"))
        .body("todos[2].completed", is(true));
  }

  @Test
  void handleEditCommandMissingIdWithFailure() {
    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new EditCommand(null, "Foobar"))
        .post("/api/edit-command")
        .then()
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body("success", is(false))
        .body("errorMessage", is("Missing property `id` in edit command."));
  }

  @Test
  void handleEditCommandEmptyIdWithFailure() {
    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new EditCommand("", "Foobar"))
        .post("/api/edit-command")
        .then()
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body("success", is(false))
        .body("errorMessage", is("Property `id` is empty in edit command."));
  }

  @Test
  void handleEditCommandMissingTitleWithFailure() {
    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new EditCommand("d2f7760d-8f03-4cb3-9176-06311cb89993", null))
        .post("/api/edit-command")
        .then()
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body("success", is(false))
        .body("errorMessage", is("Missing property `title` in edit command."));
  }

  @Test
  @Order(6)
  void handleEditCommandEmptyTitleDestroyTodoWithSuccess() {
    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new EditCommand("d2f7760d-8f03-4cb3-9176-06311cb89993", ""))
        .post("/api/edit-command")
        .then()
        .statusCode(Status.OK.getStatusCode())
        .body("success", is(true));

    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new TodosQuery())
        .post("/api/todos-query")
        .then()
        .statusCode(Status.OK.getStatusCode())
        .contentType(is(ContentType.JSON.toString()))
        .body("todos", hasSize(2))
        .body("todos[0].id", is("119e6785-8ffc-42e0-8df6-dbc64881f2b7"))
        .body("todos[0].title", is("Taste JavaScript"))
        .body("todos[0].completed", is(true))
        .body("todos[1].id", is(any(String.class)))
        .body("todos[1].title", is("Foobar"))
        .body("todos[1].completed", is(true));
  }

  @Test
  @Order(7)
  void handleDestroyCommandWithSuccess() {
    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new DestroyCommand("119e6785-8ffc-42e0-8df6-dbc64881f2b7"))
        .post("/api/destroy-command")
        .then()
        .statusCode(Status.OK.getStatusCode())
        .body("success", is(true));

    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new TodosQuery())
        .post("/api/todos-query")
        .then()
        .statusCode(Status.OK.getStatusCode())
        .contentType(is(ContentType.JSON.toString()))
        .body("todos", hasSize(1))
        .body("todos[0].id", is(any(String.class)))
        .body("todos[0].title", is("Foobar"))
        .body("todos[0].completed", is(true));
  }

  @Test
  void handleDestroyCommandMissingIdWithFailure() {
    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new EmptyCommand())
        .post("/api/destroy-command")
        .then()
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body("success", is(false))
        .body("errorMessage", is("Missing property `id` in destroy command."));
  }

  @Test
  void handleDestroyCommandEmptyIdWithFailure() {
    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new DestroyCommand(""))
        .post("/api/destroy-command")
        .then()
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body("success", is(false))
        .body("errorMessage", is("Property `id` is empty in destroy command."));
  }

  @Test
  @Order(8)
  void handleClearCompletedCommandWithSuccess() throws Exception {
    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new ClearCompletedCommand())
        .post("/api/clear-completed-command")
        .then()
        .statusCode(Status.OK.getStatusCode())
        .body("success", is(true));

    given()
        .when()
        .accept(ContentType.JSON)
        .contentType(ContentType.JSON)
        .body(new TodosQuery())
        .post("/api/todos-query")
        .then()
        .statusCode(Status.OK.getStatusCode())
        .contentType(is(ContentType.JSON.toString()))
        .body("todos", hasSize(0));
  }
}
