import React from 'react';
import Slide from './Slide';

class Slider extends React.Component {


    loadSlides() {
        return [
            {
                backUrl: "http://lorempixel.com/800/600/abstract/"
            },
            {
                backUrl: "http://lorempixel.com/800/600/animals/"
            },
            {
                backUrl: "http://lorempixel.com/800/600/sports/"
            }

        ]
    }


    constructor(props) {
        super(props);

        this.animationThreshold = 50;
        this.sliderWidth = 800; //default width for first render
        this.autoSlideInterval = 4000; //ms

        this.autoSlider = setInterval(() => this.moveRight(), this.autoSlideInterval);
        this.state = {
            current: 0,
            data: this.loadSlides(),
            animation: {
                inProgress: false,
                target: 0,
                ticksPassed: 0
            }
        };

        this.moveLeft = this.moveLeft.bind(this);
        this.moveRight = this.moveRight.bind(this);
        this.mouseOver = this.mouseOver.bind(this);
        this.mouseOut = this.mouseOut.bind(this);

    }


    mouseOver() {
        clearInterval(this.autoSlider);
    }

    mouseOut() {
        this.autoSlider = setInterval(() => this.moveRight(), this.autoSlideInterval);
    }

    componentDidMount() {
        const width = document.getElementById('slider-wrapper').clientWidth;
        this.sliderWidth = width;
    }

    componentWillUnmount() {
        clearInterval(this.animatior);
    }

    animationProcess() {
        if (this.state.animation.inProgress == false)return;
        let isLast = this.state.animation.ticksPassed + 1 == 20;

        if (isLast)clearInterval(this.animatior);
        this.setState((prev, prop) => ({
            current: isLast ? prev.animation.target : prev.current,
            data: prev.data,
            animation: {
                inProgress: isLast ? false : prev.animation.inProgress,
                target: prev.animation.target,
                ticksPassed: isLast ? 0 : prev.animation.ticksPassed + 1
            }

        }));

    }

    animateAndChange(target) {
        this.animatior = setInterval(() => this.animationProcess(), this.animationThreshold);
        this.setState({
            animation: {
                inProgress: true,
                target: target,
                ticksPassed: 0
            }
        })
    }

    moveLeft() {
        let target = this.state.current - 1 < 0 ? this.state.data.length - 1 : this.state.current - 1;
        this.animateAndChange(target)
    }

    moveRight() {
        let target = this.state.current + 1 > this.state.data.length - 1 ? 0 : this.state.current + 1;
        this.animateAndChange(target)
    }

    render() {
        return (
            <div id="slider-wrapper" onMouseEnter={this.mouseOver} onMouseLeave={this.mouseOut}>
                <ul id="react-slider" className="slider" style={this.style()}>
                    {this.state.data.map((slide, i) => <Slide key={i} backUrl={slide.backUrl}/>)}
                </ul>
                <button className="prev" onClick={this.moveLeft}> &#x2190;</button>
                <button className="next" onClick={this.moveRight}> &#x2192; </button>
            </div>
        )
    }

    style() {

        let animationSign = this.state.current < this.state.animation.target ? 1 : -1;
        let animationOffset = this.state.animation.inProgress ? (this.sliderWidth / 20 * Math.abs(this.state.current - this.state.animation.target) * this.state.animation.ticksPassed) * animationSign : 0;

        return {
            width: this.sliderWidth * this.state.data.length,
            left: '-' + parseInt(this.state.current * this.sliderWidth + animationOffset) + 'px'
        };
    }
}

export default Slider;

