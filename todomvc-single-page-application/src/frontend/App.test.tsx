import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import { render, screen, waitFor } from '@testing-library/react';

import App from './App';
import { MessageHandling } from '../contract/MessageHandling';

test('renders todos title', async () => {
  const messageHandlerMock: MessageHandling = {
    handleClearCompletedCommand: jest.fn(),
    handleDestroyCommand: jest.fn(),
    handleEditCommand: jest.fn(),
    handleNewTodoCommand: jest.fn(),
    handleToggleAllCommand: jest.fn(),
    handleToggleCommand: jest.fn(),
    handleTodosQuery: jest.fn(),
  };
  render(
    <Router>
      <App messageHandling={messageHandlerMock} />
    </Router>
  );

  await waitFor(() => {
    const titleElement = screen.getByText(/todos/i);
    expect(titleElement).toBeInTheDocument();
  });
});
