import React, { useCallback, useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';

import { MessageHandling } from '../contract/MessageHandling';
import { TodoFilter } from './types';
import TodosController from './TodosController';
import { TodosQueryResult } from '../contract/messages/queries';

export type TodosPageProps = Readonly<{
  messageHandling: MessageHandling;
}>;

function TodosPage({ messageHandling }: TodosPageProps) {
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

  const [todosQueryResult, setTodosQueryResult] = useState<TodosQueryResult>();
  const handleToggleAll = useCallback(
    async (completed) => {
      await messageHandling.handleToggleAllCommand({ completed });
      const result = await messageHandling.handleTodosQuery({});
      setTodosQueryResult(result);
    },
    [messageHandling]
  );
  const handleNewTodo = useCallback(
    async (title) => {
      await messageHandling.handleNewTodoCommand({ title });
      const result = await messageHandling.handleTodosQuery({});
      setTodosQueryResult(result);
    },
    [messageHandling]
  );
  const handleToggle = useCallback(
    async (id) => {
      await messageHandling.handleToggleCommand({ id });
      const result = await messageHandling.handleTodosQuery({});
      setTodosQueryResult(result);
    },
    [messageHandling]
  );
  const handleEdit = useCallback(
    async (id, title) => {
      await messageHandling.handleEditCommand({ id, title });
      const result = await messageHandling.handleTodosQuery({});
      setTodosQueryResult(result);
    },
    [messageHandling]
  );
  const handleDestroy = useCallback(
    async (id) => {
      await messageHandling.handleDestroyCommand({ id });
      const result = await messageHandling.handleTodosQuery({});
      setTodosQueryResult(result);
    },
    [messageHandling]
  );
  const handleClearCompleted = useCallback(async () => {
    await messageHandling.handleClearCompletedCommand({});
    const result = await messageHandling.handleTodosQuery({});
    setTodosQueryResult(result);
  }, [messageHandling]);

  useEffect(() => {
    (async () => {
      const result = await messageHandling.handleTodosQuery({});
      setTodosQueryResult(result);
    })();
  }, [messageHandling]);

  return (
    <TodosController
      filter={filter}
      {...todosQueryResult}
      onToggleAll={handleToggleAll}
      onNewTodo={handleNewTodo}
      onToggle={handleToggle}
      onEdit={handleEdit}
      onDestroy={handleDestroy}
      onClearCompleted={handleClearCompleted}
    />
  );
}

export default TodosPage;
