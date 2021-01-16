/*
 * TodoMVC - Distributed
 * Copyright (c) 2021 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.distributed;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;

class HttpJsonClient {
  private final String baseUrl;

  HttpJsonClient(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  <I, O> O execute(String endpoint, I request, Class<O> responseType)
      throws IOException, InterruptedException {
    var client = HttpClient.newHttpClient();
    var body = new Gson().toJson(request);
    var httpRequest =
        HttpRequest.newBuilder(URI.create(baseUrl + endpoint))
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(body))
            .build();
    var response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    return new Gson().fromJson(response.body(), responseType);
  }
}
