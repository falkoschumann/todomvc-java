module de.muspellheim.todomvc.backend {
  requires static lombok;
  requires de.muspellheim.messages;
  requires de.muspellheim.todomvc.contract;
  requires com.google.gson;

  exports de.muspellheim.todomvc.backend;
  exports de.muspellheim.todomvc.backend.adapters;
  exports de.muspellheim.todomvc.backend.messagehandlers;
}
