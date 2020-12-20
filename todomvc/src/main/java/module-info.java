module de.muspellheim.todomvc {
  requires transitive de.muspellheim.todomvc.backend;
  requires transitive de.muspellheim.todomvc.frontend;
  requires static lombok;

  exports de.muspellheim.todomvc;
}
