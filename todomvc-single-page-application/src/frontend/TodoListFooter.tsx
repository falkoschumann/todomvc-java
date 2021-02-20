import React from 'react';
import { Link } from 'react-router-dom';

import { TodoFilter } from './types';

export type TodoListFooterProps = Readonly<{
  activeCount?: number;
  completedCount?: number;
  filter?: TodoFilter;
  onClearCompleted?: () => void;
}>;

function TodoListFooter({
  activeCount = 0,
  completedCount = 0,
  filter = TodoFilter.All,
  onClearCompleted,
}: TodoListFooterProps) {
  if (activeCount + completedCount <= 0) {
    return null;
  }

  return (
    <footer className="footer">
      <span className="todo-count">
        <strong>{activeCount}</strong> item{activeCount !== 1 ? 's' : null} left
      </span>
      <ul className="filters">
        <li>
          <Link className={filter === TodoFilter.All ? 'selected' : undefined} to="/">
            All
          </Link>
        </li>
        <li>
          <Link className={filter === TodoFilter.Active ? 'selected' : undefined} to="/active">
            Active
          </Link>
        </li>
        <li>
          <Link className={filter === TodoFilter.Completed ? 'selected' : undefined} to="/completed">
            Completed
          </Link>
        </li>
      </ul>
      {completedCount <= 0 ? null : (
        <button className="clear-completed" onClick={onClearCompleted}>
          Clear completed
        </button>
      )}
    </footer>
  );
}

export default TodoListFooter;
