import React, { useState, useEffect } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import Form from "react-bootstrap/Form";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import TaskService from "../services/TaskService";
import { format } from "date-fns";

function EditTask({ task, onClose }) {
    const [show, setShow] = useState(true);
    const [taskName, setTaskName] = useState("");
    const [endTime, setEndTime] = useState(null);
    const [status, setStatus] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    useEffect(() => {
        setTaskName(task.task_name);
        setEndTime(new Date(task.end_time));
        setStatus(task.status);
    }, [task]);

    const handleClose = () => {
        setShow(false);
        onClose();
    };

    const handleSaveChanges = () => {
        const formattedEndTime = endTime ? format(endTime, "yyyy-MM-dd") : null;
        const updatedTaskData = {
            task_name: taskName,
            end_time: formattedEndTime,
            status: status,
        };

        TaskService.editTask(task.id, updatedTaskData)
            .then((response) => {
                console.log("Task updated successfully:", response.data);
                handleClose();
            })
            .catch((error) => {
                console.error("Error updating task:", error);
                setErrorMessage(
                    "The syntax is wrong, date should be in the future or task should have more than 3 words. Please try again."
                );
            });
    };

    return (
        <Modal show={show} onHide={handleClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>Edit Task</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}
                <Form>
                    <Row>
                        <Col md={7}>
                            <Form.Group className="mb-3" controlId="Input1">
                                <Form.Label>Task Name</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder="Enter Task Name"
                                    autoFocus
                                    value={taskName}
                                    onChange={(e) =>
                                        setTaskName(e.target.value)
                                    }
                                />
                            </Form.Group>
                        </Col>
                        <Col md={5}>
                            <Form.Group className="mb-3" controlId="Input4">
                                <Form.Label>End Date</Form.Label>
                                <DatePicker
                                    selected={endTime}
                                    onChange={(date) => setEndTime(date)}
                                    dateFormat="yyyy/MM/dd"
                                    placeholderText="Select End Date"
                                    className="form-control"
                                />
                            </Form.Group>
                        </Col>
                    </Row>
                    <Row>
                        <Col md={12}>
                            <Form.Group className="mb-3" controlId="Input2">
                                <Form.Label>Status</Form.Label>
                                <Form.Select
                                    value={status}
                                    onChange={(e) => setStatus(e.target.value)}
                                >
                                    <option value="">Select Status</option>

                                    <option value="IN_PROGRESS">
                                        IN_PROGRESS
                                    </option>
                                    <option value="COMPLETED">COMPLETED</option>
                                </Form.Select>
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

export default EditTask;
