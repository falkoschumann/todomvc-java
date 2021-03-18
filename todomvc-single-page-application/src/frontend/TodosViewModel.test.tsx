import React, { ReactNode } from 'react';
import { MemoryRouter } from 'react-router';
import { act, renderHook, WrapperComponent } from '@testing-library/react-hooks';

import { MessageHandling } from '../contract/MessageHandling';
import { useTodosViewModel } from './TodosViewModel';
import { TodoFilter } from './types';
import MessageHandlingProvider from './MessageHandlingProvider';

const todo1 = { id: '1', title: 'Taste JavaScript', completed: true };
const todo2 = { id: '2', title: 'Buy a unicorn', completed: false };

function mockMessageHandling(): MessageHandling {
  return {
    handleClearCompletedCommand: jest.fn(),
    handleDestroyCommand: jest.fn(),
    handleEditCommand: jest.fn(),
    handleNewTodoCommand: jest.fn(),
    handleToggleAllCommand: jest.fn(),
    handleToggleCommand: jest.fn(),
    handleTodosQuery: jest.fn(() =>
      Promise.resolve({
        todos: [todo1, todo2],
      })
    ),
  };
}

function createWrapper(messageHandling: MessageHandling): WrapperComponent<ReactNode> {
  return ({ children }) => (
    <MessageHandlingProvider messageHandling={messageHandling}>
      <MemoryRouter>{children}</MemoryRouter>
    </MessageHandlingProvider>
  );
}

test('Initial State', async () => {
  const messageHandling = mockMessageHandling();
  const wrapper = createWrapper(messageHandling);
  const { result, waitForNextUpdate } = renderHook(() => useTodosViewModel(), { wrapper });

  await waitForNextUpdate();

  expect(result.current.todos).toEqual([todo1, todo2]);
  expect(result.current.filter).toEqual(TodoFilter.All);
});

test('Toggle all', async () => {
  const messageHandling = mockMessageHandling();
  const wrapper = createWrapper(messageHandling);
  const { result, waitForNextUpdate } = renderHook(() => useTodosViewModel(), { wrapper });
  await waitForNextUpdate();

  act(() => {
    result.current.toggleAll(true);
  });
  await waitForNextUpdate();

  expect(messageHandling.handleToggleAllCommand).toBeCalledTimes(1);
  expect(messageHandling.handleToggleAllCommand).toBeCalledWith({ completed: true });
  expect(messageHandling.handleTodosQuery).toBeCalledTimes(2);
});

test('New todo', async () => {
  const messageHandling = mockMessageHandling();
  const wrapper = createWrapper(messageHandling);
  const { result, waitForNextUpdate } = renderHook(() => useTodosViewModel(), { wrapper });
  await waitForNextUpdate();

  act(() => {
    result.current.newTodo('Lorem ipsum');
  });
  await waitForNextUpdate();

  expect(messageHandling.handleNewTodoCommand).toBeCalledTimes(1);
  expect(messageHandling.handleNewTodoCommand).toBeCalledWith({ title: 'Lorem ipsum' });
  expect(messageHandling.handleTodosQuery).toBeCalledTimes(2);
});

test('Toggle', async () => {
  const messageHandling = mockMessageHandling();
  const wrapper = createWrapper(messageHandling);
  const { result, waitForNextUpdate } = renderHook(() => useTodosViewModel(), { wrapper });
  await waitForNextUpdate();

  act(() => {
    result.current.toggle('1');
  });
  await waitForNextUpdate();

  expect(messageHandling.handleToggleCommand).toBeCalledTimes(1);
  expect(messageHandling.handleToggleCommand).toBeCalledWith({ id: '1' });
  expect(messageHandling.handleTodosQuery).toBeCalledTimes(2);
});

test('Edit', async () => {
  const messageHandling = mockMessageHandling();
  const wrapper = createWrapper(messageHandling);
  const { result, waitForNextUpdate } = renderHook(() => useTodosViewModel(), { wrapper });
  await waitForNextUpdate();

  act(() => {
    result.current.edit('2', 'Buy a horse');
  });
  await waitForNextUpdate();

  expect(messageHandling.handleEditCommand).toBeCalledTimes(1);
  expect(messageHandling.handleEditCommand).toBeCalledWith({ id: '2', title: 'Buy a horse' });
  expect(messageHandling.handleTodosQuery).toBeCalledTimes(2);
});

test('Destroy', async () => {
  const messageHandling = mockMessageHandling();
  const wrapper = createWrapper(messageHandling);
  const { result, waitForNextUpdate } = renderHook(() => useTodosViewModel(), { wrapper });
  await waitForNextUpdate();

  act(() => {
    result.current.destroy('2');
  });
  await waitForNextUpdate();

  expect(messageHandling.handleDestroyCommand).toBeCalledTimes(1);
  expect(messageHandling.handleDestroyCommand).toBeCalledWith({ id: '2' });
  expect(messageHandling.handleTodosQuery).toBeCalledTimes(2);
});

test('Clear completed', async () => {
  const messageHandling = mockMessageHandling();
  const wrapper = createWrapper(messageHandling);
  const { result, waitForNextUpdate } = renderHook(() => useTodosViewModel(), { wrapper });
  await waitForNextUpdate();

  act(() => {
    result.current.clearCompleted();
  });
  await waitForNextUpdate();

  expect(messageHandling.handleClearCompletedCommand).toBeCalledTimes(1);
  expect(messageHandling.handleClearCompletedCommand).toBeCalledWith({});
  expect(messageHandling.handleTodosQuery).toBeCalledTimes(2);
});
