import React from 'react';

import { MessageHandling } from '../contract/MessageHandling';
import TodosView from './TodosView';
import { useTodosViewModel } from './TodosViewModel';

export type TodosPageProps = Readonly<{
  messageHandling: MessageHandling;
}>;

function TodosPage({ messageHandling }: TodosPageProps) {
  const viewModel = useTodosViewModel(messageHandling);
  return <TodosView {...viewModel} />;
}

export default TodosPage;
