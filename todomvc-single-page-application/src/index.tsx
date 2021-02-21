import React from 'react';
import ReactDOM from 'react-dom';

import { BackendProxy } from './backend/BackendProxy';
import App from './frontend/App';
import reportWebVitals from './reportWebVitals';
import './index.css';

const backend = new BackendProxy();
const frontend = <App messageHandling={backend} />;
ReactDOM.render(frontend, document.getElementById('root'));

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
