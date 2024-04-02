
import React, { useState } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import Form from "react-bootstrap/Form";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import DatePicker from "react-datepicker"; // Import DatePicker
import "react-datepicker/dist/react-datepicker.css"; // Import styles
import TaskService from "../services/TaskService"; // Thay đổi import
import { useParams } from "react-router-dom";
import { format } from "date-fns";

function CreateTask({ onClose }) {
    
    const [show, setShow] = useState(true);
    const [taskName, setTaskName] = useState(""); // Thay đổi tên state thành taskName
    const [endTime, setEndDate] = useState(null);
    const [validated, setValidated] = useState(false); // Add validated state
    const { id } = useParams();
    const [errorMessage, setErrorMessage] = useState("");

    const handleClose = () => {
        setShow(false);
        onClose();
    };

    const handleSaveChanges = (e) => {
        const form = e.currentTarget;
        if (form.checkValidity() === false) {
            e.preventDefault();
            e.stopPropagation();
        }
        setValidated(true);

        const formattedEndTime = endTime ? format(endTime, "yyyy-MM-dd") : null;
        const taskData = {
            task_name: taskName, // Thay đổi key thành name
            end_time: formattedEndTime,

        }
        TaskService.createTask(taskData, id)
            .then((response) => {
                console.log("Task created successfully:", response.data);
                handleClose();
            })
            .catch((error) => {
                console.error("Error creating task:", error);
                setErrorMessage(
                    "The syntax is wrong, date should be in the future or task should have more than 3 words. Please try again."
                );
            });
    };
    return (
        <Modal show={show} onHide={handleClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>New Task</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}
                <Form
                    noValidate
                    validated={validated}
                    onSubmit={handleSaveChanges}
                >
                    {" "}
                    {/* Add noValidate and validated props */}
                    <Row>
                        <Col md={7}>
                            <Form.Group className="mb-3" controlId="Input1">
                                <Form.Label>Name Task</Form.Label>{" "}
                                {/* Thay đổi nhãn thành "Name Task" */}
                                <Form.Control
                                    type="text"
                                    placeholder="Task Name"
                                    autoFocus
                                    value={taskName}
                                    onChange={(e) =>
                                        setTaskName(e.target.value)
                                    } // Cập nhật state taskName
                                    required // Add required attribute
                                />
                                <Form.Control.Feedback type="invalid">
                                    Please provide a task name.
                                </Form.Control.Feedback>
                            </Form.Group>
                        </Col>
                        <Col md={5}>
                            <Form.Group className="mb-3" controlId="Input4">
                                <Form.Label>Date End</Form.Label>
                                <DatePicker
                                    defaultValue={null}
                                    selected={endTime}
                                    onChange={(date) => setEndDate(date)}
                                    dateFormat="yyyy/MM/dd"
                                    placeholderText="YYYY/MM/DD"
                                    className="form-control"
                                    required // Add required attribute
                                />
                                <Form.Control.Feedback type="invalid">
                                    Please provide a valid end date.
                                </Form.Control.Feedback>
                            </Form.Group>
                        </Col>
                    </Row>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Close
                </Button>
                <Button variant="primary" onClick={handleSaveChanges}>
                    Save Changes
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default CreateTask;
