import React, { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import DatePicker from 'react-datepicker'; // Import DatePicker
import 'react-datepicker/dist/react-datepicker.css'; // Import styles
import TaskService from '../services/TaskService'; // Thay đổi import
import { useParams } from 'react-router-dom';


function CreateTask({ onClose }) { // Thay đổi tên hàm thành CreateTask
    const [show, setShow] = useState(true);
    const [task_name, setTaskName] = useState(''); // Thay đổi tên state thành taskName
    const [end_time, setEndDate] = useState(null);
    const { id } = useParams();

    const handleClose = () => {
        setShow(false);
        onClose();
    };

    const handleSaveChanges = () => {
        const taskData = {
            task_name: task_name, // Thay đổi key thành name
            end_time: end_time
        };

        TaskService.createTask(taskData, id)
            .then(response => {
                console.log('Task created successfully:', response.data);
                handleClose();
            })
            .catch(error => {
                console.error('Error creating task:', error);
            });
    };
    return (
        <Modal show={show} onHide={handleClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>New Task</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Row>
                        <Col md={7}>
                            <Form.Group className="mb-3" controlId="Input1">
                                <Form.Label>Name Task</Form.Label> {/* Thay đổi nhãn thành "Name Task" */}
                                <Form.Control type="Title" placeholder="Task Name" autoFocus
                                    value={task_name}
                                    onChange={e => setTaskName(e.target.value)} // Cập nhật state taskName
                                />
                            </Form.Group>
                        </Col>
                        <Col md={5}>
                            <Form.Group className="mb-3" controlId="Input4">
                                <Form.Label>Date End</Form.Label>
                                <DatePicker
                                    selected={end_time}
                                    onChange={date => setEndDate(date)}
                                    dateFormat="dd/MM/yyyy"
                                    placeholderText="DD/MM/YYYY"
                                    className="form-control"
                                />
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