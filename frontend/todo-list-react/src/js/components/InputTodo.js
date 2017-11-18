import React from "react"
import { connect } from "react-redux"

export default class InputTodo extends React.Component{

	constructor(props) {
		super(props);
		this.state = { value: "" };

	}	

	inputChanged(event){
		this.setState({value:event.target.value});
	}

	addTodo(){
		this.props.addTodo(this.state.value);
		this.setState( {value: "" } )
	}

	render() {
		return <div>
			<input value={this.state.value} onChange={this.inputChanged.bind(this)}/><button onClick={this.addTodo.bind(this) }>Add</button>
		</div>

	}

}


