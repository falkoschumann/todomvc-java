import React from 'react';
import { render, fireEvent, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';

import TodoListHeader from './TodoListHeader';

it('New todo', () => {
  const handleNewTodo = jest.fn();
  render(<TodoListHeader onNewTodo={handleNewTodo} />);
  const input = screen.getByPlaceholderText('What needs to be done?') as HTMLInputElement;

  userEvent.type(input, 'Taste JavaScript');
  fireEvent.keyDown(input, { key: 'Enter' });

  expect(input.value).toEqual('');
  expect(handleNewTodo).toBeCalledTimes(1);
  expect(handleNewTodo).toBeCalledWith('Taste JavaScript');
});
