import React from 'react'
import { connect } from "react-redux"
import { Link } from 'react-router'
import TodoList from '../components/TodoList'
import InputTodo from '../components/InputTodo'
import { bindActionCreators } from 'redux'
import {addTodo, deleteTodo} from '../actions/todoActions'


@connect((store) => {
	return {
    items : store.todo.items
  }
})
export default class TodoListPage extends React.Component{

	constructor(props) {
		super(props);
		
	}

	render(){
		const {items, dispatch} = this.props;
		let addTodoItem = bindActionCreators(addTodo, dispatch);
		let removeTodoItem = bindActionCreators(deleteTodo, dispatch);
		return <div>
			<TodoList items={items} deleteTodo={removeTodoItem}/>
			<InputTodo addTodo={addTodoItem}/>
			<Link to="/list"> Go to list with filter </Link>
		 </div>
	}

}