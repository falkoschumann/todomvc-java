/*
 * TodoMVC - Frontend
 * Copyright (c) 2021 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.frontend;

import de.muspellheim.todomvc.contract.MessageHandling;
import java.net.URL;
import java.util.Properties;

public class ViewModelFactory {
  private static MessageHandling messageHandling;
  private static URL iconUrl;
  private static Properties appProperties;

  public static void initMessageHandling(MessageHandling messageHandling) {
    ViewModelFactory.messageHandling = messageHandling;
  }

  public static void initIconUrl(URL iconUrl) {
    ViewModelFactory.iconUrl = iconUrl;
  }

  public static void initAppProperties(Properties appProperties) {
    ViewModelFactory.appProperties = appProperties;
  }

  public static MainViewModel getMainViewModel() {
    return new MainViewModel(messageHandling);
  }

  public static InfoViewModel getInfoViewModel() {
    return new InfoViewModel(iconUrl, appProperties);
  }
}
