import React, { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import DatePicker from 'react-datepicker'; // Import DatePicker
import 'react-datepicker/dist/react-datepicker.css'; // Import styles

function CreateProgram({ onClose }) {
  const [show, setShow] = useState(true);
  const [startDate, setStartDate] = useState(null); // Changed to null
  const [endDate, setEndDate] = useState(null); // Changed to null

  const handleClose = () => {
    setShow(false);
    onClose();
  };

  return (
    <Modal show={show} onHide={handleClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>New Program</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form>
          <Row>
            <Col md={7}>
              <Form.Group className="mb-3" controlId="Input1">
                <Form.Label>Name Program</Form.Label>
                <Form.Control type="Title" placeholder="Title Name" autoFocus />
              </Form.Group>
            </Col>
            <Col md={5}>
              <Form.Group className="mb-3" controlId="Input4">
                <Form.Label>Date End</Form.Label>
                <DatePicker
                  selected={endDate} // Changed prop name
                  onChange={date => setEndDate(date)} // Changed prop name
                  dateFormat="dd/MM/yyyy" // Changed date format
                  placeholderText="DD/MM/YYYY" // Changed placeholder
                  className="form-control" // Added Bootstrap class
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
        <Button variant="primary" onClick={handleClose}>
          Save Changes
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default CreateProgram;