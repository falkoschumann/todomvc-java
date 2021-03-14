import { useCallback, useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';

import { Todo, TodoId } from '../contract/data';
import { TodoFilter } from './types';
import { useMessageHandling } from './MessageHandlingProvider';

export type TodosViewModel = Readonly<{
  filter: TodoFilter;
  todos: readonly Todo[];
  toggleAll: (completed: boolean) => void;
  newTodo: (title: string) => void;
  toggle: (id: TodoId) => void;
  edit: (id: TodoId, title: string) => void;
  destroy: (id: TodoId) => void;
  clearCompleted: () => void;
}>;

export function useTodosViewModel(): TodosViewModel {
  const messageHandling = useMessageHandling();
  const location = useLocation();
  let filter: TodoFilter;
  switch (location.pathname) {
    case '/active':
      filter = TodoFilter.Active;
      break;
    case '/completed':
      filter = TodoFilter.Completed;
      break;
    default:
      filter = TodoFilter.All;
      break;
  }

  const [todos, setTodos] = useState<readonly Todo[]>([]);

  const toggleAll = useCallback(
    async (completed) => {
      await messageHandling.handleToggleAllCommand({ completed });
      const result = await messageHandling.handleTodosQuery({});
      setTodos(result.todos);
    },
    [messageHandling]
  );

  const newTodo = useCallback(
    async (title) => {
      await messageHandling.handleNewTodoCommand({ title });
      const result = await messageHandling.handleTodosQuery({});
      setTodos(result.todos);
    },
    [messageHandling]
  );

  const toggle = useCallback(
    async (id) => {
      await messageHandling.handleToggleCommand({ id });
      const result = await messageHandling.handleTodosQuery({});
      setTodos(result.todos);
    },
    [messageHandling]
  );

  const edit = useCallback(
    async (id, title) => {
      await messageHandling.handleEditCommand({ id, title });
      const result = await messageHandling.handleTodosQuery({});
      setTodos(result.todos);
    },
    [messageHandling]
  );

  const destroy = useCallback(
    async (id) => {
      await messageHandling.handleDestroyCommand({ id });
      const result = await messageHandling.handleTodosQuery({});
      setTodos(result.todos);
    },
    [messageHandling]
  );

  const clearCompleted = useCallback(async () => {
    await messageHandling.handleClearCompletedCommand({});
    const result = await messageHandling.handleTodosQuery({});
    setTodos(result.todos);
  }, [messageHandling]);

  useEffect(() => {
    (async () => {
      const result = await messageHandling.handleTodosQuery({});
      setTodos(result.todos);
    })();
  }, [messageHandling]);

  return {
    filter,
    todos,
    toggleAll,
    newTodo,
    toggle,
    edit,
    destroy,
    clearCompleted,
  };
}
