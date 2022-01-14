import * as React from 'react';
import TextField from '@mui/material/TextField';
import AdapterDateFns from '@mui/lab/AdapterDateFns';
import LocalizationProvider from '@mui/lab/LocalizationProvider';
import DatePicker from '@mui/lab/DatePicker';
import {Col, Form, Row} from "react-bootstrap";
function SearchDatePicker() {
    const [value, setValue] = React.useState([null, null]);
    return (
        <div>
            <Form>
                <div>
                    <Form.Label><b>Pick a date</b></Form.Label>
                </div>
                <br/>
                <LocalizationProvider dateAdapter={AdapterDateFns}>
                    <Row>
                        <Col>
                            <div>
                                <DatePicker
                                    label="Start Date"
                                    value={value}
                                    onChange={(newValue) => {
                                        setValue(newValue);
                                    }}
                                    renderInput={(params) => <TextField {...params} />}
                                />
                            </div>
                        </Col>
                        <Col>
                            <div className='pd-date-to'>
                                <DatePicker
                                    label="End Date"
                                    value={value}
                                    onChange={(newValue) => {
                                        setValue(newValue);
                                    }}
                                    renderInput={(params) => <TextField {...params} />}
                                />
                            </div>
                        </Col>
                    </Row>
                </LocalizationProvider>
            </Form>

        </div>
    );
}
export default SearchDatePicker;