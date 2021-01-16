import React, { useCallback, useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';

import { MessageHandling } from '../domain/MessageHandling';
import { TodoFilter } from './types';
import TodosController from './TodosController';
import { TodosQueryResult } from '../domain/messages/queries';

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
  const handleToggleAllCommand = useCallback(
    async (command) => {
      await messageHandling.handleToggleAllCommand(command);
      const result = await messageHandling.handleTodosQuery({});
      setTodosQueryResult(result);
    },
    [messageHandling]
  );
  const handleNewTodoCommand = useCallback(
    async (command) => {
      await messageHandling.handleNewTodoCommand(command);
      const result = await messageHandling.handleTodosQuery({});
      setTodosQueryResult(result);
    },
    [messageHandling]
  );
  const handleToggleCommand = useCallback(
    async (command) => {
      await messageHandling.handleToggleCommand(command);
      const result = await messageHandling.handleTodosQuery({});
      setTodosQueryResult(result);
    },
    [messageHandling]
  );
  const handleEditCommand = useCallback(
    async (command) => {
      await messageHandling.handleEditCommand(command);
      const result = await messageHandling.handleTodosQuery({});
      setTodosQueryResult(result);
    },
    [messageHandling]
  );
  const handleDestroyCommand = useCallback(
    async (command) => {
      await messageHandling.handleDestroyCommand(command);
      const result = await messageHandling.handleTodosQuery({});
      setTodosQueryResult(result);
    },
    [messageHandling]
  );
  const handleClearCompletedCommand = useCallback(
    async (command) => {
      await messageHandling.handleClearCompletedCommand(command);
      const result = await messageHandling.handleTodosQuery({});
      setTodosQueryResult(result);
    },
    [messageHandling]
  );

  useEffect(() => {
    (async () => {
      const result = await messageHandling.handleTodosQuery({});
      setTodosQueryResult(result);
    })();
  }, [messageHandling]);

  return (
    <TodosController
      filter={filter}
      todosQueryResult={todosQueryResult}
      onToggleAllCommand={handleToggleAllCommand}
      onNewTodoCommand={handleNewTodoCommand}
      onToggleCommand={handleToggleCommand}
      onEditCommand={handleEditCommand}
      onDestroyCommand={handleDestroyCommand}
      onClearCompletedCommand={handleClearCompletedCommand}
    />
  );
}

export default TodosPage;
