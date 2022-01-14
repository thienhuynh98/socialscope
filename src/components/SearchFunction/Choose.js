import React from "react";
import {Col, Form, Row} from "react-bootstrap";

function Choose()
{
    return(
        <Form>
            <Form.Label><b>Platform</b></Form.Label>
            {['checkbox'].map(() => (
                <div>
                    <Row>
                        <Col>
                            <Form.Check
                                id='twitter'
                                label="Twitter"
                            />
                        </Col>
                        <Col>
                            <Form.Check
                                id='youtube'
                                label="YouTube"
                            />
                        </Col>
                        <Col>
                            <Form.Check
                                id='reddit'
                                label="Reddit"
                            />
                        </Col>
                    </Row>
                </div>
            ))}
        </Form>
    )
}
export default Choose