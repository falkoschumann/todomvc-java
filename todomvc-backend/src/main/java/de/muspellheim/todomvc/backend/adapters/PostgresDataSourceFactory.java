/*
 * TodoMVC - Backend
 * Copyright (c) 2021 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.adapters;

import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;

public class PostgresDataSourceFactory {
  public static DataSource create(
      String host, int port, String database, String user, String password) {
    var dataSource = new PGSimpleDataSource();
    dataSource.setServerNames(new String[] {host});
    dataSource.setPortNumbers(new int[] {port});
    dataSource.setDatabaseName(database);
    dataSource.setUser(user);
    dataSource.setPassword(password);
    return dataSource;
  }
}
