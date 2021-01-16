import React from 'react';
import { Switch, Route } from 'react-router-dom';

import TodosPage from './portals/TodosPage';
import InfoFooter from './portals/InfoFooter';
import { BackendProxy } from './providers/BackendProxy';
import './App.css';

const messageHandling = new BackendProxy();

function App() {
  return (
    <React.StrictMode>
      <section className="todoapp">
        <Switch>
          <Route path="/">
            <TodosPage messageHandling={messageHandling} />
          </Route>
        </Switch>
      </section>
      <InfoFooter />
    </React.StrictMode>
  );
}

export default App;
