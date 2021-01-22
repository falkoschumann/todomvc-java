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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JdbcTodoRepositoryTests {
  private DataSource dataSource;

  @BeforeEach
  void setUp() throws SQLException {
    dataSource = H2DataSourceFactory.createFile(Paths.get("./build/test"), "sa", "sa");
    initDatabase(dataSource);
  }

  @AfterEach
  void tearDown() throws SQLException {
    try (var connection = dataSource.getConnection()) {
      try (var statement = connection.createStatement()) {
        System.out.println("Drop table...");
        statement.executeUpdate("DROP TABLE todos;");
      }
    }
  }

  @Test
  void load() throws SQLException {
    var repository = new JdbcTodoRepository(dataSource);

    var todos = repository.load();

    assertEquals(createTestData(), todos);
  }

  @Test
  void store() throws SQLException {
    var repository = new JdbcTodoRepository(dataSource);
    var todos = createTestData();

    repository.store(todos);

    var actualTodos = repository.load();
    assertEquals(createTestData(), actualTodos);
  }

  private static void initDatabase(DataSource dataSource) throws SQLException {
    try (var connection = dataSource.getConnection()) {
      connection.setAutoCommit(true);
      try (var statement = connection.createStatement()) {
        System.out.println("Create table...");
        statement.executeUpdate(
            "CREATE TABLE todos ("
                + "id VARCHAR(255) NOT NULL,"
                + "title VARCHAR(255) NOT NULL,"
                + "completed BOOLEAN NOT NULL);");
        System.out.println("Create records...");
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
