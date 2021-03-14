import React, { useState } from 'react';

import { Todo, TodoId } from '../contract/data';
import { TodoFilter } from './types';
import TodoListHeader from './TodoListHeader';
import TodoList from './TodoList';
import TodoItem from './TodoItem';
import TodoListFooter from './TodoListFooter';

export type TodosViewProps = Readonly<{
  filter?: TodoFilter;
  todos?: readonly Todo[];
  onToggleAll?: (completed: boolean) => void;
  onNewTodo?: (title: string) => void;
  onToggle?: (id: TodoId) => void;
  onEdit?: (id: TodoId, title: string) => void;
  onDestroy?: (id: TodoId) => void;
  onClearCompleted?: () => void;
}>;

function TodosView({
  filter = TodoFilter.All,
  todos = [],
  onToggleAll,
  onNewTodo,
  onToggle,
  onEdit,
  onDestroy,
  onClearCompleted,
}: TodosViewProps) {
  const [editing, setEditing] = useState<TodoId>();

  const activeCount = todos.filter((it) => !it.completed).length;
  const completedCount = todos.filter((it) => it.completed).length;

  function handleEdit(todo: Todo) {
    setEditing(todo.id);
  }

  function handleSave(todo: Todo, title: string) {
    setEditing(undefined);
    onEdit?.(todo.id, title);
  }

  function handleCancel() {
    setEditing(undefined);
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
        onToggle={() => onToggle?.(it.id)}
        onEdit={() => handleEdit(it)}
        onSave={(title) => handleSave(it, title)}
        onCancel={handleCancel}
        onDestroy={() => onDestroy?.(it.id)}
      />
    ));

  return (
    <>
      <TodoListHeader onNewTodo={onNewTodo} />
      {todoItems.length === 0 ? null : (
        <TodoList allCompleted={activeCount === 0} onToggleAll={onToggleAll}>
          {todoItems}
        </TodoList>
      )}
      {todos.length === 0 ? null : (
        <TodoListFooter
          activeCount={activeCount}
          completedCount={completedCount}
          filter={filter}
          onClearCompleted={onClearCompleted}
        />
      )}
    </>
  );
}

export default TodosView;
