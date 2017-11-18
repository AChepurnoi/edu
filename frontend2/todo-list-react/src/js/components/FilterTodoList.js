import React from "react"
import { connect } from "react-redux"



export default class FilterTodoList extends React.Component {

  constructor(props) {
    super(props);
    this.state = { filter: "" }
    
  }

  search(event){
  	this.setState({filter: event.target.value})
  }

  render() {

  	let {items} = this.props;

  	if(this.state.filter) items = items.filter((item) => item.text.includes(this.state.filter))
    return <div>
      <input onChange={this.search.bind(this)}/>
      <ul>
        {items.map((item) =><li key={item.id}>{item.text}</li>)}
      </ul>
    </div>
  }
}

