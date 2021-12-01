import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';


class PreviewRow extends React.Component {

  render() {
    const shadeClass = (this.props.number % 2 === 0) ? 'dark-shade' : 'light-shade';
    const cellContent = []
    for (const field in this.props.displayFields) {
      // populate cell content with data fields named in displayFields
      cellContent.push(<td key={field}>{this.props.data[field]}</td>);
    }
    const toggleButton = (
      <button className={''} onClick={() => this.props.descriptionToggleHandler(this.props.number)}>
        {this.props.descriptionMinimized ? '+' : '-'}
      </button>
    );
    // append toggle button column
    cellContent.push(<td key="button">{toggleButton}</td>);
    
    const CellRow = () => {return (
      <tr key={this.props.number} className={'data-row ' + shadeClass}>
        {cellContent}
      </tr>
    );}

    const DescriptionRow = () => {return (
      <tr className={'description-row ' + shadeClass}>
        <td colSpan={Object.keys(this.props.displayFields).length + 1} style={
          this.props.descriptionMinimized ? {display: 'none'} : {visibility: 'visible', width: '100%'}
          }>
          <div style={{fontWeight: 'bold'}}>{this.props.data.title}</div>
          <div>
            {
            (this.props.data.text.length === 0 && this.props.data.has_embedded_media === true) 
            ? "[Body consists of an embedded link or media]" : this.props.data.text
            }
          </div>
        </td>
      </tr>
    );}
    
    return (
      <>
        <CellRow />
        <DescriptionRow />
      </>
    );
  }

}

class PreviewHead extends React.Component {

  renderSortButtons(field) {
    return(
        <div className={'sort-button-container'}>
        <button onClick={() => this.props.sortHandler(field, true)}>
          {'\u2191'}
        </button>
        <button onClick={() => this.props.sortHandler(field, false)}>
          {'\u2193'}
        </button>
      </div>
    );
  }

  render() {
    let cols = [];
    Object.keys(this.props.displayFields).forEach((key) => cols.push(
      <th key={key}>
        <div className='head-flex-container'>
          <div className={'sort-text-container'}>
            {this.props.displayFields[key]}
          </div>
          {this.renderSortButtons(key)}
        </div>
      </th>
    ));
    cols.push(
      <th key={"button"} height={40} width={45}>
        <div className='head-flex-container'>
          Show{'\n'}text
        </div>
      </th>
    ); /* append toggle button column label */
    return (
      <tr height={35}>
        {cols}
      </tr>
    );
  }
}

class PreviewTable extends React.Component {

  constructor(props) {
    super(props);
    let rowData = [];
    for (let i = 0; i < this.props.posts.length; i++) {
      rowData[i] = {
        post: this.props.posts[i],
        descriptionMinimized: true,
      }
    }
    this.state = {
      rows: rowData,
    };
    this.sortRowsByField = this.sortRowsByField.bind(this);
    this.toggleRowDescription = this.toggleRowDescription.bind(this);
  }

  sortRowsByField(by, ascending) {
    this.setState({
      rows: this.state.rows.slice().sort((a, b) => this.fieldSort(a.post, b.post, by, ascending))
    });
  }

  fieldSort(a, b, sortBy, sortAscending, fields = Object.keys(this.props.displayFields)) {
    const sortDirection = sortAscending ? 1 : -1;
    if (a[sortBy] > b[sortBy]) {
      return sortDirection * 1;
    }
    else if (a[sortBy] < b[sortBy]) {
      return sortDirection * -1;
    }
    else if (fields.length > 1) {
      const fieldsRemaining = fields.splice(fields.indexOf(sortBy), 1);
      return this.fieldSort(a, b, fieldsRemaining.keys()[0], sortAscending, fieldsRemaining);
    }
    else {
      return 0
    }
  }

  componentDidMount() {
    this.sortRowsByField("has_embedded_media", true);
  }

  toggleRowDescription(i) {
    let rowData = this.state.rows.slice();
    rowData[i].descriptionMinimized = !rowData[i].descriptionMinimized
    this.setState({
      rows: rowData
    });
  }

  renderRow(i) {
    return (
      <PreviewRow 
      key={i}
      number={i}
      data={this.state.rows[i].post}
      displayFields={this.props.displayFields}
      descriptionMinimized={this.state.rows[i].descriptionMinimized}
      descriptionToggleHandler={this.toggleRowDescription}
      />
    );
  }

  render() {

    let renderedRows = [];
    for (let i = 0; i < this.state.rows.length; i++) {
      renderedRows.push(this.renderRow(i));
    }
    return (
      <table className="paleBlueRows">
        <thead>
          <PreviewHead
            displayFields={this.props.displayFields}
            sortHandler = {this.sortRowsByField}
          />
        </thead>
        <tbody>
          {renderedRows}
        </tbody>
      </table>
    );
  }

}

class App extends React.Component {

  constructor(props) {
    super(props);
    let data = require('./aggregateResults.json');
    this.state = {
      posts: data.posts,
      displayFieldNames: 
        { /* format = json_field: display_name */
          platform: "Platform",
          positive_votes: "Likes",
          comment_count: "Comment Count"
        },
      mounted:false,
    };
  }

  componentDidMount() {
    this.setState({mounted:true});
  }

  render() {
    return (
      <div className='outerWrapper'>
        <div className='main-title'>
          SocialScope Display Demo
        </div>
        <PreviewTable 
          posts={this.state.posts}
          displayFields={this.state.displayFieldNames}
          parentMounted={this.state.mounted}
        />
      </div>
    );
  }

}

ReactDOM.render(
  <App />,
  document.getElementById('root')
);