import React from 'react';

class Slide extends React.Component {


    constructor(props) {
        super(props)
        
    }

    render() {
        return (<li className="slide">
                <img className="img-background" src={this.props.backUrl}/>
            </li>
        )
    }

 

}

export default Slide;
