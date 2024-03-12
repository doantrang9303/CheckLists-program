import React, { useState } from 'react';
import Button from 'react-bootstrap/Button';
import CreateProgram from './CreateProgram';
import axios from 'axios';
import Pagination from 'react-bootstrap/Pagination';
import Form from 'react-bootstrap/Form';
import Dropdown from 'react-bootstrap/Dropdown';
import DropdownButton from 'react-bootstrap/DropdownButton';


const callApi = async () => {
    const accessToken = localStorage.getItem('access_token');
    console.log(accessToken)
    try {
        const response = await axios.post('http', {}, {
            headers: {
                'Authorization': `Bearer ${accessToken}` // Include the access token in the Authorization header
            }
        })

        alert(response.data);
        // Handle your response data
    } catch (error) {
        console.error('Error making API call:', error);
    }
};

function Home() {
    const [showCreateProgram, setShowCreateProgram] = useState(false);

    // Function to handle the click event of the "checkbox-all"
    const handleCheckAll = (event) => {
        const checkboxes = document.querySelectorAll('.CheckOption input[type="checkbox"]');
        checkboxes.forEach((checkbox) => {
            checkbox.checked = event.target.checked;
        });
    };

    const handleCreateProgramClick = () => {
        setShowCreateProgram(true);
    };

    const handleCloseCreateProgram = () => {
        setShowCreateProgram(false);
    };
    const [selectedOption, setSelectedOption] = useState(""); // State to track selected option

    const handleSelectChange = (event) => {
        setSelectedOption(event.target.value); // Update selected option
    };


    return (
        <nav className="p-3 bg-light">
            <ul style={{ justifyContent: 'flex-start', display: 'flex' }}>
                <div className="me-2 dropdown">
                    <Form.Select aria-label="Default select example" onChange={handleSelectChange} value={selectedOption}>
                        <option>Program</option>
                        <option value="1">One</option>
                        <option value="2">Two</option>
                        <option value="3">Three</option>
                    </Form.Select>
                </div>
                <Button
                    style={{ width: '140px' }}
                    type="button"
                    className="text-center btn btn-custom1 btn-outline-dark btn-lg fs-6 ms-2"
                    onClick={handleCreateProgramClick}
                >
                    New Program
                </Button>
                {selectedOption && ( // Conditionally render "Show Task" button
                    <Button
                        style={{ width: '140px' }}
                        type="button"
                        className="text-center btn btn-custom1 btn-outline-dark btn-lg fs-6 ms-2"
                    >
                        Show Task
                    </Button>
                )}
            </ul>
            <ul style={{ display: 'flex', whiteSpace: 'nowrap' }}>
                <li style={{ display: 'inline-block' }}>
                    <Button style={{ width: '150px' }}
                        type="button"
                        className="text-center btn btn-custom1 btn-outline-dark btn-lg fs-6 ms-2"
                        onClick={handleCreateProgramClick}
                    >
                        Name Program
                    </Button>
                </li>
                <li style={{ display: 'inline-block' }}>
                    <Button style={{ width: '140px' }}
                        type="button"
                        className="text-center btn btn-custom1 btn-outline-dark btn-lg fs-6 ms-2"
                        onClick={handleCreateProgramClick}
                    >
                        Deadline
                    </Button>
                </li>
                <li style={{ display: 'inline-block' }}>
                    <DropdownButton id="dropdown-basic-button" title="Status"       className=" outline-dark    ms-2"
                         >
                        <Dropdown.Item href="#/action-1">Done</Dropdown.Item>
                        <Dropdown.Item href="#/action-2">Not Done</Dropdown.Item>
                    </DropdownButton>
            </li>
            <li style={{ display: 'inline-block', marginLeft: 'auto' }}>
                <Button style={{ width: '125px' }}
                    type="button"
                    className="text-center btn btn-custom1 btn-outline-dark btn-lg fs-6 ms-2"
                >
                    Create new
                </Button>
            </li>
            <li style={{ display: 'inline-block' }}>
                <Button style={{ width: '125px' }}
                    type="button"
                    className="text-center btn btn-custom1 btn-outline-dark btn-lg fs-6 ms-2"
                >
                    Delete
                </Button>
            </li>
            <li style={{ display: 'inline-block' }}>
                <Button style={{ width: '125px' }}
                    type="button"
                    className="text-center btn btn-custom1 btn-outline-dark btn-lg fs-6 ms-2"
                >
                    Import File
                </Button>
            </li>
        </ul>


            { showCreateProgram && <CreateProgram onClose={handleCloseCreateProgram} /> }
            <table className="table caption-top bg-white rounded" >
                <thead>
                    <tr>
                        <th scoope="col">
                            <Form className="ClickAll" style={{ display: 'flex', justifyContent: 'center' }}>
                                <Form.Check
                                    type="checkbox"
                                    id="check-all"
                                    onChange={handleCheckAll} // Attach the event handler here
                                />
                            </Form>
                        </th>
                        <th scope="col">No</th>
                        <th scope="col">Task Name</th>
                        <th scope="col">Create Date</th>
                        <th scope="col">Deadline</th>
                        <th scope="col">Status</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <th>
                            <Form className="CheckOption" style={{ display: 'flex', justifyContent: 'center' }}>
                                <Form.Check type="checkbox" />
                            </Form>
                        </th>
                        <th scope="row">1</th>
                        <td>Hire Student</td>
                        <td>25/4/2024</td>
                        <td>25/4/2024</td>
                        <td>Not Done</td>
                    </tr>
                    <tr>
                        <th>
                            <Form className="CheckOption" style={{ display: 'flex', justifyContent: 'center' }}>
                                <Form.Check type="checkbox" />
                            </Form>
                        </th>
                        <th scope="row">1</th>
                        <td>Hire Student</td>
                        <td>25/4/2024</td>
                        <td>25/4/2024</td>
                        <td>Not Done</td>
                    </tr>
                    <tr>
                        <th>
                            <Form className="CheckOption" style={{ display: 'flex', justifyContent: 'center' }}>
                                <Form.Check type="checkbox" />
                            </Form>
                        </th>
                        <th scope="row">1</th>
                        <td>Hire Student</td>
                        <td>25/4/2024</td>
                        <td>25/4/2024</td>
                        <td>Not Done</td>
                    </tr>

                    <tr>
                        <th>
                            <Form className="CheckOption" style={{ display: 'flex', justifyContent: 'center' }}>
                                <Form.Check type="checkbox" />
                            </Form>
                        </th>
                        <th scope="row">1</th>
                        <td>Hire Student</td>
                        <td>25/4/2024</td>
                        <td>25/4/2024</td>
                        <td>Not Done</td>
                    </tr>
                    <tr>
                        <th>
                            <Form className="CheckOption" style={{ display: 'flex', justifyContent: 'center' }}>
                                <Form.Check type="checkbox" />
                            </Form>
                        </th>
                        <th scope="row">1</th>
                        <td>Hire Student</td>
                        <td>25/4/2024</td>
                        <td>25/4/2024</td>
                        <td>Not Done</td>
                    </tr>
                </tbody>
            </table>
            <Pagination style={{ display: 'flex', justifyContent: 'center' }}>
                <Pagination.First />
                <Pagination.Prev />
                <Pagination.Item>{1}</Pagination.Item>
                <Pagination.Item>{2}</Pagination.Item>
                <Pagination.Ellipsis />
                <Pagination.Item>{19}</Pagination.Item>
                <Pagination.Item>{20}</Pagination.Item>
                <Pagination.Next />
                <Pagination.Last />
            </Pagination>

        </nav >
    )
}
export default Home