/*
 * TodoMVC - Distributed Application
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.distributed;

import de.muspellheim.todomvc.frontend.MainView;
import de.muspellheim.todomvc.frontend.ViewModelFactory;
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
    ViewModelFactory.initMessageHandling(backendProxy);

    var url = getClass().getResource("/app.png");
    ViewModelFactory.initIconUrl(url);

    var properties = new Properties();
    try (InputStream in = getClass().getResourceAsStream("/app.properties")) {
      properties.load(in);
    }
    ViewModelFactory.initAppProperties(properties);

    var frontend = MainView.create(primaryStage);
    frontend.run();
  }
}
