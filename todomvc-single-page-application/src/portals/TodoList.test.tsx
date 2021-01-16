import React from 'react';
import { render, fireEvent, screen } from '@testing-library/react';

import TodoList from './TodoList';

it('Toggle all', () => {
  const handleToggleAll = jest.fn();
  render(<TodoList onToggleAll={handleToggleAll} />);
  const input = screen.getByTitle('Toggle all') as HTMLInputElement;

  fireEvent.click(input);

  expect(handleToggleAll).toBeCalledTimes(1);
});
