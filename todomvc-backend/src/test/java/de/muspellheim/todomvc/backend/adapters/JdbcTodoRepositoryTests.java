/*
 * TodoMVC - Backend
 * Copyright (c) 2021 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.muspellheim.todomvc.contract.data.Todo;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JdbcTodoRepositoryTests {
  private JdbcTodoRepository repository;

  @BeforeEach
  void setUp() throws SQLException {
    var dataSource = H2DataSourceFactory.createFile(Paths.get("./build/test"), "sa", "sa");
    repository = new JdbcTodoRepository(dataSource);
    repository.createSchema();
    insertTestData(dataSource);
  }

  @Test
  void load() throws SQLException {
    var todos = repository.load();

    assertEquals(createTestData(), todos);
  }

  @Test
  void store() throws SQLException {
    var todos = createTestData();

    repository.store(todos);

    var actualTodos = repository.load();
    assertEquals(createTestData(), actualTodos);
  }

  private static void insertTestData(DataSource dataSource) throws SQLException {
    try (var connection = dataSource.getConnection()) {
      try (var statement = connection.createStatement()) {
        statement.executeUpdate("DELETE FROM todos;");
        statement.executeUpdate(
            "INSERT INTO todos (id, title, completed)"
                + " VALUES ('119e6785-8ffc-42e0-8df6-dbc64881f2b7', 'Taste JavaScript', TRUE);");
        statement.executeUpdate(
            "INSERT INTO todos (id, title, completed)"
                + " VALUES ('d2f7760d-8f03-4cb3-9176-06311cb89993', 'Buy a unicorn', FALSE);");
      }
    }
  }

  private static List<Todo> createTestData() {
    return List.of(
        new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
        new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false));
  }
}
