import React from "react"
import { connect } from "react-redux"



export default class TodoList extends React.Component {

  constructor(props) {
    super(props);
    
  }

  removeItem(id){
    this.props.deleteTodo(id);
  }

  render() {
    return <div>
      <ul>
        {this.props.items.map((item) =><li key={item.id}>{item.text} <button onClick={this.removeItem.bind(this,item.id)}>Remove</button></li>)}
      </ul>
    </div>
  }
}

