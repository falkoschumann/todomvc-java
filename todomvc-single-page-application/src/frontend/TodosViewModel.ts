import { MessageHandling } from '../contract/MessageHandling';
import { TodoFilter } from './types';
import { Todo, TodoId } from '../contract/data';
import { useLocation } from 'react-router-dom';
import { useCallback, useEffect, useState } from 'react';

export type TodosViewModel = Readonly<{
  filter: TodoFilter;
  todos: readonly Todo[];
  onToggleAll: (completed: boolean) => void;
  onNewTodo: (title: string) => void;
  onToggle: (id: TodoId) => void;
  onEdit: (id: TodoId, title: string) => void;
  onDestroy: (id: TodoId) => void;
  onClearCompleted: () => void;
}>;

export function useTodosViewModel(messageHandling: MessageHandling): TodosViewModel {
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

  const onToggleAll = useCallback(
    async (completed) => {
      await messageHandling.handleToggleAllCommand({ completed });
      const result = await messageHandling.handleTodosQuery({});
      setTodos(result.todos);
    },
    [messageHandling]
  );

  const onNewTodo = useCallback(
    async (title) => {
      await messageHandling.handleNewTodoCommand({ title });
      const result = await messageHandling.handleTodosQuery({});
      setTodos(result.todos);
    },
    [messageHandling]
  );

  const onToggle = useCallback(
    async (id) => {
      await messageHandling.handleToggleCommand({ id });
      const result = await messageHandling.handleTodosQuery({});
      setTodos(result.todos);
    },
    [messageHandling]
  );

  const onEdit = useCallback(
    async (id, title) => {
      await messageHandling.handleEditCommand({ id, title });
      const result = await messageHandling.handleTodosQuery({});
      setTodos(result.todos);
    },
    [messageHandling]
  );

  const onDestroy = useCallback(
    async (id) => {
      await messageHandling.handleDestroyCommand({ id });
      const result = await messageHandling.handleTodosQuery({});
      setTodos(result.todos);
    },
    [messageHandling]
  );

  const onClearCompleted = useCallback(async () => {
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
    onToggleAll,
    onNewTodo,
    onToggle,
    onEdit,
    onDestroy,
    onClearCompleted,
  };
}
