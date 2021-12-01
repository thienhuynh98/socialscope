
import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';

function Square(props) {
  return (
    <button className="square" onClick={props.onClick} style={props.style}>
      {props.value}
    </button>
  );
}

class Board extends React.Component {

  renderSquare(i) {
    return (
      <Square
        value={this.props.squares[i]}
        onClick={() => this.props.onClick(i)}
        style={this.props.highlight.includes(i) ? {backgroundColor:"yellow"} : {backgroundColor:"transparent"}}
      />
    );
  }

  renderRow(squares) {
    return(
      <div className="board-row">
        {squares}
      </div>
    );
  }

  render() {
    var rows = [];
    for (let i = 0; i < 3; i++) {
      var cols = [];
      for (let j = 0; j < 3; j++) {
        cols.push(this.renderSquare(3*i + j));
      }
      rows.push(this.renderRow(cols));
    }
    return (
      <div>
        {rows}
      </div>
    );
  }
}

class Game extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      history: [{
        squares: Array(9).fill(null),
        lastMove: null,
      }],
      stepNumber: 0,
      xIsNext: true,
      sortMovesDescending: false,
    };
  }

 handleClick(i) {
    const history = this.state.history.slice(0, this.state.stepNumber + 1);
    const current = history[history.length - 1];
    const squares = current.squares.slice();
    if (squares[i] || calculateWinner(squares)) {
      return;
    }
    squares[i] = this.state.xIsNext ? 'X' : 'O';
    this.setState({
      history: history.concat([{
        squares: squares,
        lastMove: i,
      }]),
      stepNumber: history.length,
      xIsNext: !this.state.xIsNext,
    });
  }

  jumpTo(step) {
    this.setState({
      stepNumber: step,
      xIsNext: (step % 2) === 0,
    });
  }

  toggleSort() {
    this.setState({
      sortMovesDescending: !this.state.sortMovesDescending,
    });
  }

  render() {
    const history = this.state.history;
    const current = history[this.state.stepNumber];
    const win = calculateWinner(current.squares);

    let moves = history.map((step, move) => {
      const desc = move ?
        'Go to move #' + move + ' (' + (step.lastMove % 3) + ',' + Math.floor(step.lastMove / 3) + ')':
        'Go to game start';
      const sty = (move === this.state.stepNumber) ? 
        {fontWeight:'bold'} : 
        {fontWeight:'normal'};
      return (
        <li key={move} style={sty}>
          <button onClick={() => this.jumpTo(move)}>{desc}</button>
        </li>
      );
    });
    if (this.state.sortMovesDescending) moves = moves.slice().reverse();
	
    const toggleButton = (
      <button onClick={() => this.toggleSort()}>
        {"Sort " + (this.state.sortMovesDescending ? "ascending" : "descending")}
      </button>
    );

    let status;
    let highlight = [];
    if (win) {
      status = 'Winner: ' + win.winner;
      highlight = win.line
    } else if (this.state.stepNumber >= 9) {
      status = 'Tie game.';
    } else {
      status = 'Next player: ' + (this.state.xIsNext ? 'X' : 'O');
    }

    return (
      <div className="game">
        <div className="game-board">
          <Board 
            squares={current.squares}
            onClick={i => this.handleClick(i)}
            highlight={highlight}
          />
        </div>
        <div className="game-info">
          <div>{status}</div>
          <div style={{padding: "25px 10px 0px 30px"}}>{toggleButton}</div>
          <ul>{moves}</ul>
        </div>
      </div>
    );
  }
}

// ========================================

ReactDOM.render(
  <Game />,
  document.getElementById('root')
);

function calculateWinner(squares) {
  const lines = [
    [0, 1, 2],
    [3, 4, 5],
    [6, 7, 8],
    [0, 3, 6],
    [1, 4, 7],
    [2, 5, 8],
    [0, 4, 8],
    [2, 4, 6],
  ];
  for (let i = 0; i < lines.length; i++) {
    const [a, b, c] = lines[i];
    if (squares[a] && squares[a] === squares[b] && squares[a] === squares[c]) {
      const win = {
        winner: squares[a],
        line: lines[i],
      }
      return win;
    }
  }
  return null;
}
