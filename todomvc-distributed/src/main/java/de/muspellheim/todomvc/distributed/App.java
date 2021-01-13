/*
 * TodoMVC - Distributed
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.distributed;

import de.muspellheim.todomvc.frontend.UserInterface;
import java.io.InputStream;
import java.util.Properties;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    // TODO Backend Host starten
    var backendProxy = new BackendProxy();
    var appIcon = getClass().getResource("/app.png");
    var appProperties = new Properties();
    try (InputStream in = getClass().getResourceAsStream("/app.properties")) {
      appProperties.load(in);
    }
    var frontend = new UserInterface(backendProxy, primaryStage, appIcon, appProperties);
    frontend.run();
  }
}
