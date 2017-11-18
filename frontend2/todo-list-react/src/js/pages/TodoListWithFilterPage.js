import React from 'react'
import { connect } from "react-redux"
import { Link } from 'react-router'
import FilterTodoList from '../components/FilterTodoList'

@connect((store) => {
	return {
    items : store.todo.items
  }
})
export default class TodoListWithFilterPage extends React.Component{

	render(){
		const todoItems = this.props.items;
		return <div>I am list page with filter 
		<FilterTodoList items={todoItems}/>
		<Link to="/"> Go to Main page </Link> </div>
	}

}