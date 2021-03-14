import React from 'react';

import TodosView from './TodosView';
import { useTodosViewModel } from './TodosViewModel';

function TodosPage() {
  const viewModel = useTodosViewModel();
  return (
    <TodosView
      filter={viewModel.filter}
      todos={viewModel.todos}
      onToggleAll={viewModel.toggleAll}
      onNewTodo={viewModel.newTodo}
      onToggle={viewModel.toggle}
      onEdit={viewModel.edit}
      onDestroy={viewModel.destroy}
      onClearCompleted={viewModel.clearCompleted}
    />
  );
}

export default TodosPage;
