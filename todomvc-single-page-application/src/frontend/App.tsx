import React from 'react';
import { Switch, Route } from 'react-router-dom';

import TodosPage from './TodosPage';
import InfoFooter from './InfoFooter';
import './App.css';
import { MessageHandling } from '../contract/MessageHandling';

export type AppProps = Readonly<{
  messageHandling: MessageHandling;
}>;

function App({ messageHandling }: AppProps) {
  return (
    <>
      <section className="todoapp">
        <Switch>
          <Route path="/">
            <TodosPage messageHandling={messageHandling} />
          </Route>
        </Switch>
      </section>
      <InfoFooter />
    </>
  );
}

export default App;
