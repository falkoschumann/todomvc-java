module de.muspellheim.todomvc.backend {
  requires static com.h2database;
  requires static java.naming;
  requires static lombok;
  requires static org.postgresql.jdbc;
  requires de.muspellheim.todomvc.contract;
  requires com.google.gson;

  exports de.muspellheim.todomvc.backend;
  exports de.muspellheim.todomvc.backend.adapters;
  exports de.muspellheim.todomvc.backend.messagehandlers;
}
