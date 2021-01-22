/*
 * TodoMVC - Backend
 * Copyright (c) 2021 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.adapters;

import java.nio.file.Path;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;

public class H2DataSourceFactory {
  public static DataSource createFile(Path file, String user, String password) {
    var dataSource = new JdbcDataSource();
    dataSource.setURL("jdbc:h2:" + file);
    dataSource.setUser(user);
    dataSource.setPassword(password);
    return dataSource;
  }
}
