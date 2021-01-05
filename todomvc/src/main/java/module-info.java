module de.muspellheim.todomvc {
  requires static lombok;
  requires de.muspellheim.todomvc.backend;
  requires de.muspellheim.todomvc.contract;
  requires de.muspellheim.todomvc.frontend;
  requires javafx.graphics;

  exports de.muspellheim.todomvc;
}
