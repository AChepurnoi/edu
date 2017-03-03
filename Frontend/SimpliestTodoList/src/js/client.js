import React from "react"
import ReactDOM from "react-dom"
import { Provider } from "react-redux"
import {Router, Route, Link, IndexRoute, browserHistory } from 'react-router'

import App from './pages/App'
import TodoListPage from './pages/TodoListPage'
import TodoListWithFilterPage from './pages/TodoListWithFilterPage'

import store from "./store"

const app = document.getElementById('app')

ReactDOM.render(
<Provider store={store}>
	<Router history={browserHistory}>
		<Route path="/"component={App}>
			 <IndexRoute component={TodoListPage} />
     		 <Route path="list" component={TodoListWithFilterPage} />
		</Route>
	</Router>
</Provider>, app);
