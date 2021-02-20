import React, { useRef } from 'react';

export type TodoListHeaderProps = Readonly<{
  onNewTodo?: (text: string) => void;
}>;

function TodoListHeader({ onNewTodo }: TodoListHeaderProps) {
  const newTodoRef = useRef<HTMLInputElement>(null);

  function handleNewTodo(event: React.KeyboardEvent<HTMLInputElement>) {
    if (newTodoRef.current == null || event.key !== 'Enter') {
      return;
    }

    event.preventDefault();
    const text = newTodoRef.current.value.trim();
    onNewTodo?.(text);
    newTodoRef.current.value = '';
  }

  return (
    <header className="header">
      <h1>todos</h1>
      <input
        ref={newTodoRef}
        className="new-todo"
        placeholder="What needs to be done?"
        autoFocus={true}
        onKeyDown={handleNewTodo}
      />
    </header>
  );
}

export default TodoListHeader;
