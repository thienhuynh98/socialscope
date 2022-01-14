import React, {useState} from "react";
import {Accordion, Container} from "react-bootstrap";

function FAQ()
{
    return (
        <div className='pd-top-2'>
            <h1 className='page-title'>Frequently Asked Question</h1>
            <Container>
                <div className='pd-top-2'>
                    <Accordion defaultActiveKey="0" flush>
                        <Accordion.Item eventKey="0">
                            <Accordion.Header><b>Who are we?</b></Accordion.Header>
                            <Accordion.Body className='faq-answer-left-margin'>
                                We are all students at Bellevue College
                            </Accordion.Body>
                        </Accordion.Item>
                        <Accordion.Item eventKey="1">
                            <Accordion.Header>
                                <b>What is this application about?</b></Accordion.Header>
                            <Accordion.Body className='faq-answer-left-margin'>
                                This application will work as a tool to scrap all the data about some topics on social media. After
                                that, it will use these to analyze and can predict what would be trends in the future.
                            </Accordion.Body>
                        </Accordion.Item>
                    </Accordion>
                </div>
            </Container>
        </div>
    );
}
export default FAQ