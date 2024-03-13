import React, { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import HomeService from './API/HomeService';

function CreateProgram({ onClose }) {
  const [show, setShow] = useState(true);
  const [programName, setProgramName] = useState(''); // State for program name
  const [endDate, setEndDate] = useState(null);

  const handleClose = () => {
    setShow(false);
    onClose();
  };

  const handleSave = () => {
    const programDTO = {
      name: programName,
      end_time: endDate,
    };

    // Call the addProgram function from HomeService to send the request
    HomeService.addProgram(programDTO)
      .then(response => {
        console.log('Program created:', response.data);
        handleClose(); // Close the modal after successful creation
      })
      .catch(error => {
        console.error('Error creating program:', error);
        // Handle error if needed
      });
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
              <Form.Group className="mb-3" controlId="inputProgramName">
                <Form.Label>Name Program</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter program name"
                  value={programName}
                  onChange={e => setProgramName(e.target.value)} // Update program name state
                  autoFocus
                />
              </Form.Group>
            </Col>
            <Col md={5}>
              <Form.Group className="mb-3" controlId="inputEndDate">
                <Form.Label>Date End</Form.Label>
                <DatePicker
                  selected={endDate}
                  onChange={date => setEndDate(date)}
                  dateFormat="yyyy-MM-dd"
                  placeholderText="YYYY-MM-DD"
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
        <Button variant="primary" onClick={handleSave}> {/* Call handleSave when Save Changes button is clicked */}
          Save Changes
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default CreateProgram;