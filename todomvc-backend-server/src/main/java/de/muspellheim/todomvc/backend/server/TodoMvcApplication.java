/*
 * TodoMVC - Backend Server
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
/*
@OpenAPIDefinition(
    info = @Info(version = "1.0", title = "TodoMVC API"),
    servers = @Server(url = "/api"))
 */
public class TodoMvcApplication extends Application {}
