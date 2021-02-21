module de.muspellheim.todomvc.contract {
  requires static lombok;
  requires com.fasterxml.jackson.annotation;

  exports de.muspellheim.todomvc.contract;
  exports de.muspellheim.todomvc.contract.data;
  exports de.muspellheim.todomvc.contract.messages.commands;
  exports de.muspellheim.todomvc.contract.messages.queries;

  opens de.muspellheim.todomvc.contract.data;
  opens de.muspellheim.todomvc.contract.messages.commands;
  opens de.muspellheim.todomvc.contract.messages.queries;
}
