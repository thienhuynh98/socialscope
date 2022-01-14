import React from "react";
import {Form} from "react-bootstrap";

function MaxResult()
{
    return (
        <div>
            <Form>
                <Form.Group className="mb-3" >
                    <Form.Label><b>Max Result</b></Form.Label>
                    <br/>
                    <Form.Control type="input" placeholder="Enter a number" />
                </Form.Group>
            </Form>
        </div>

    )
}
export default MaxResult