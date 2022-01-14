import React from "react";
import {Form} from "react-bootstrap";

function QuerryInput()
{
    return (
        <div>
            <Form>
                <Form.Group className="mb-3" >
                    <Form.Label><b>Something to search</b></Form.Label>
                    <Form.Control type="input" placeholder="keyword or phrase" />
                </Form.Group>
            </Form>
        </div>

    )
}
export default QuerryInput