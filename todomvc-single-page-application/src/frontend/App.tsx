import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';

import { MessageHandling } from '../contract/MessageHandling';
import TodosPage from './TodosPage';
import InfoFooter from './InfoFooter';
import './App.css';

export type AppProps = Readonly<{
  messageHandling: MessageHandling;
}>;

function App({ messageHandling }: AppProps) {
  return (
    <React.StrictMode>
      <Router>
        <section className="todoapp">
          <Switch>
            <Route path="/">
              <TodosPage messageHandling={messageHandling} />
            </Route>
          </Switch>
        </section>
        <InfoFooter />
      </Router>
    </React.StrictMode>
  );
}

export default App;
