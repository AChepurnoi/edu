import React from 'react'
import { connect } from "react-redux"



export default class App extends React.Component{


	render(){
		return <div>I Am an app
			<div>{this.props.children}</div>
		</div>
	}

}