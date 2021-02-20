module de.muspellheim.todomvc.distributed {
  requires static lombok;
  requires de.muspellheim.todomvc.contract;
  requires de.muspellheim.todomvc.frontend;
  requires java.net.http;
  requires com.google.gson;
  requires javafx.graphics;

  exports de.muspellheim.todomvc.distributed;
}
