import React from 'react';

export type TodoListProps = Readonly<{
  allCompleted?: boolean;
  children?: React.ReactElement[] | React.ReactElement | null;
  onToggleAll?: (completed: boolean) => void;
}>;

function TodoList({ allCompleted = false, children, onToggleAll }: TodoListProps) {
  function handleToggleAll() {
    onToggleAll?.(!allCompleted);
  }

  return (
    <section className="main">
      <input
        title="Toggle all"
        id="toggle-all"
        className="toggle-all"
        type="checkbox"
        checked={allCompleted}
        onChange={handleToggleAll}
      />
      <label htmlFor="toggle-all">Mark all as complete</label>
      <ul className="todo-list">{children}</ul>
    </section>
  );
}

export default TodoList;
