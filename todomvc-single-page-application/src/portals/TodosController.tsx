import React, { useState } from 'react';

import {
  ClearCompletedCommand,
  DestroyCommand,
  EditCommand,
  NewTodoCommand,
  ToggleAllCommand,
  ToggleCommand,
} from '../domain/messages/commands';
import { TodosQueryResult } from '../domain/messages/queries';
import { Todo, TodoId } from '../domain/data';
import { TodoFilter } from './types';
import TodoListHeader from './TodoListHeader';
import TodoList from './TodoList';
import TodoItem from './TodoItem';
import TodoListFooter from './TodoListFooter';

export type TodosControllerProps = Readonly<{
  filter?: TodoFilter;
  todosQueryResult?: TodosQueryResult;
  onToggleAllCommand?: (command: ToggleAllCommand) => void;
  onNewTodoCommand?: (command: NewTodoCommand) => void;
  onToggleCommand?: (command: ToggleCommand) => void;
  onEditCommand?: (command: EditCommand) => void;
  onDestroyCommand?: (command: DestroyCommand) => void;
  onClearCompletedCommand?: (command: ClearCompletedCommand) => void;
}>;

function TodosController({
  filter = TodoFilter.All,
  todosQueryResult = { todos: [] },
  onToggleAllCommand,
  onNewTodoCommand,
  onToggleCommand,
  onEditCommand,
  onDestroyCommand,
  onClearCompletedCommand,
}: TodosControllerProps) {
  const [editing, setEditing] = useState<TodoId>();

  const todos = todosQueryResult.todos;
  const activeCount = todos.filter((it) => !it.completed).length;
  const completedCount = todos.filter((it) => it.completed).length;

  function handleToggleAll(completed: boolean) {
    onToggleAllCommand?.({ completed });
  }

  function handleNewTodo(title: string) {
    onNewTodoCommand?.({ title });
  }

  function handleToggle(todo: Todo) {
    onToggleCommand?.({ id: todo.id });
  }

  function handleEdit(todo: Todo) {
    setEditing(todo.id);
  }

  function handleSave(todo: Todo, title: string) {
    setEditing(undefined);
    onEditCommand?.({ id: todo.id, title });
  }

  function handleCancel() {
    setEditing(undefined);
  }

  function handleDestroy(todo: Todo) {
    onDestroyCommand?.({ id: todo.id });
  }

  function handleClearCompleted() {
    onClearCompletedCommand?.({});
  }

  const todoItems = todos
    .filter(
      (it) =>
        filter === TodoFilter.All ||
        (filter === TodoFilter.Active && !it.completed) ||
        (filter === TodoFilter.Completed && it.completed)
    )
    .map((it) => (
      <TodoItem
        key={it.id}
        todo={it}
        editing={editing === it.id}
        onToggle={() => handleToggle(it)}
        onEdit={() => handleEdit(it)}
        onSave={(title) => handleSave(it, title)}
        onCancel={handleCancel}
        onDestroy={() => handleDestroy(it)}
      />
    ));

  return (
    <>
      <TodoListHeader onNewTodo={handleNewTodo} />
      {todoItems.length === 0 ? null : (
        <TodoList allCompleted={activeCount === 0} onToggleAll={handleToggleAll}>
          {todoItems}
        </TodoList>
      )}
      {todos.length === 0 ? null : (
        <TodoListFooter
          activeCount={activeCount}
          completedCount={completedCount}
          filter={filter}
          onClearCompleted={handleClearCompleted}
        />
      )}
    </>
  );
}

export default TodosController;
