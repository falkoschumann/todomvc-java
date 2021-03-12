import React from 'react';

import TodosView from './TodosView';
import { useTodosViewModel } from './TodosViewModel';

function TodosPage() {
  const viewModel = useTodosViewModel();
  return <TodosView {...viewModel} />;
}

export default TodosPage;
