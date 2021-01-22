/*
 * TodoMVC - Backend
 * Copyright (c) 2021 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.adapters;

import de.muspellheim.todomvc.backend.TodoRepository;
import de.muspellheim.todomvc.contract.data.Todo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import lombok.NonNull;

public class JdbcTodoRepository implements TodoRepository {
  private final DataSource dataSource;

  public JdbcTodoRepository(@NonNull DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void createSchema() throws SQLException {
    try (var connection = dataSource.getConnection()) {
      try (var statement = connection.createStatement()) {
        statement.executeUpdate(
            "CREATE TABLE IF NOT EXISTS todos ("
                + "id VARCHAR(255) NOT NULL,"
                + "title VARCHAR(255) NOT NULL,"
                + "completed BOOLEAN NOT NULL);");
      }
    }
  }

  @Override
  public List<Todo> load() throws SQLException {
    try (var connection = dataSource.getConnection()) {
      var retrieveAllSql = "SELECT id, title, completed FROM todos;";
      try (var statement = connection.prepareStatement(retrieveAllSql)) {
        var todos = new ArrayList<Todo>();
        var resultSet = statement.executeQuery();
        while (resultSet.next()) {
          var id = resultSet.getString(1);
          var title = resultSet.getString(2);
          var completed = resultSet.getBoolean(3);
          todos.add(new Todo(id, title, completed));
        }
        return todos;
      }
    }
  }

  @Override
  public void store(List<Todo> todos) throws SQLException {
    try (var connection = dataSource.getConnection()) {
      var autoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);
      var deleteAllSql = "DELETE FROM todos;";
      try (var statement = connection.prepareStatement(deleteAllSql)) {
        statement.executeUpdate();
      }
      var createSql = "INSERT INTO todos (id, title, completed) VALUES (?, ?, ?);";
      try (var statement = connection.prepareStatement(createSql)) {
        for (var todo : todos) {
          statement.setString(1, todo.getId());
          statement.setString(2, todo.getTitle());
          statement.setBoolean(3, todo.isCompleted());
          statement.addBatch();
        }
        statement.executeBatch();
      }
      connection.commit();
      connection.setAutoCommit(autoCommit);
    }
  }
}
