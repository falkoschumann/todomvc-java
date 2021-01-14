import React from 'react';
import { Router } from 'react-router-dom';
import { createMemoryHistory } from 'history';
import { render, fireEvent, screen } from '@testing-library/react';

import TodoListFooter from './TodoListFooter';

it('Clear completed', () => {
  const history = createMemoryHistory();
  const handleClearCompleted = jest.fn();
  render(
    <Router history={history}>
      <TodoListFooter completedCount={1} onClearCompleted={handleClearCompleted} />
    </Router>
  );
  const button = screen.getByText('Clear completed') as HTMLButtonElement;

  fireEvent.click(button);

  expect(handleClearCompleted).toBeCalledTimes(1);
});
