/*
 * TodoMVC - Contract
 * Copyright (c) 2021 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.contract.messages.commands;

import lombok.NonNull;
import lombok.Value;

@Value
public class Failure implements CommandStatus {
  @NonNull String errorMessage;
}
