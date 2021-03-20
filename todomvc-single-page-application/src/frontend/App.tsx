import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';

import './App.css';
import TodosPage from './TodosPage';
import InfoFooter from './InfoFooter';

function App() {
  return (
    <React.StrictMode>
      <Router>
        <section className="todoapp">
          <Switch>
            <Route path="/" component={TodosPage} />
          </Switch>
        </section>
        <InfoFooter />
      </Router>
    </React.StrictMode>
  );
}

export default App;
