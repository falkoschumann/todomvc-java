module de.muspellheim.todomvc.backend.distributed {
  requires de.muspellheim.messages;
  requires de.muspellheim.todomvc.frontend;
  requires java.net.http;
  requires com.google.gson;
  requires static lombok;

  exports de.muspellheim.todomvc.distributed;
}
