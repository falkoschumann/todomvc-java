import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import { render, screen, waitFor } from '@testing-library/react';

import App from './App';

test('renders todos title', async () => {
  render(
    <Router>
      <App />
    </Router>
  );

  await waitFor(() => {
    const titleElement = screen.getByText(/todos/i);
    expect(titleElement).toBeInTheDocument();
  });
});
