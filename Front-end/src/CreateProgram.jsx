import React, { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import DatePicker from 'react-datepicker'; // Import DatePicker
import 'react-datepicker/dist/react-datepicker.css'; // Import styles
import ProgramService from './services/ProgramService';
import { useAuth } from 'oidc-react';  

function CreateProgram({ onClose }) {
  const [show, setShow] = useState(true);
  const [programName, setProgramName] = useState(''); // State for program name
  const [endDate, setEndDate] = useState(null);
  const [validated, setValidated] = useState(false); // State for form validation
  const auth = useAuth(); 
  const [errorMessage, setErrorMessage] = useState('');
  const handleClose = () => {
    setShow(false);
    onClose();
  };

  
  const handleSaveChanges = (event) => {
    const form = event.currentTarget;
    if (form.checkValidity() === false) {
      event.preventDefault();
      event.stopPropagation();
    } else {
      const programData = {
        name: programName,
        endtime: endDate
      };
  
      ProgramService.createProgram(programData, auth.userData?.profile.preferred_username)
        .then(response => {
          console.log('Program created successfully:', response.data);
          handleClose();
        })
        .catch(error => {
          console.error('Error creating program:', error);
          setErrorMessage('The syntax is wrong, date should be in the future or task should have more than 3 words. Please try again.');
        });
    }
    setValidated(true);
  };

  return (
    <Modal show={show} onHide={handleClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>New Program</Modal.Title>
      </Modal.Header>
      <Modal.Body>
      {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
        <Form noValidate validated={validated}>
          <Row>
            <Col md={7}>
              <Form.Group className="mb-3" controlId="Input1">
                <Form.Label>Name Program</Form.Label>
                <Form.Control 
                  type="Title" 
                  placeholder="Program Name" 
                  autoFocus 
                  value={programName} 
                  onChange={e => setProgramName(e.target.value)} // Update programName state
                  required // Add required attribute
                />
                <Form.Control.Feedback type="invalid">
                  Please provide a program name.
                </Form.Control.Feedback>
              </Form.Group>
            </Col>
            <Col md={5}>
              <Form.Group className="mb-3" controlId="Input4">
                <Form.Label>Date End</Form.Label>
                <DatePicker
                  selected={endDate} // Changed prop name
                  onChange={date => setEndDate(date)} // Changed prop name
                  dateFormat="yyyy/MM/dd" // Changed date format
                  placeholderText="YYYY/MM/DD" // Changed placeholder
                  className="form-control" // Added Bootstrap class
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
        <Button variant="primary" onClick={handleSaveChanges}> {/* Call handleSaveChanges on click */}
          Save Changes
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default CreateProgram;