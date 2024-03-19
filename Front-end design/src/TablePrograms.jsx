import React, { useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import CreateProgram from './CreateProgram';
import axios from 'axios';
import Pagination from 'react-bootstrap/Pagination';
import Form from 'react-bootstrap/Form';
import Dropdown from 'react-bootstrap/Dropdown';
import DropdownButton from 'react-bootstrap/DropdownButton';
import { fetchAllProgram } from './services/ProgramService';
import { useAuth } from 'oidc-react';  

const callApi = async () => {
    const accessToken = localStorage.getItem('access_token');
    console.log(accessToken)
    try {
        const response = await axios.post(process.env.REACT_APP_KEYCLOAK_VERIFY , {}, {
            headers: {
                'Authorization': `Bearer ${accessToken}` // Include the access token in the Authorization header
            }
        })

        alert(response.data);
        // Handle your response data
    } catch (error) {

       if (error.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx
        console.log(error.response.data);
        console.log(error.response.status);
        console.log(error.response.headers);
    } else if (error.request) {
        // The request was made but no response was received
        // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
        // http.ClientRequest in node.js
        console.log(error.request);
    } else {
        // Something happened in setting up the request that triggered an Error
        console.log('Error', error.message);
    }
    console.log(error.config);
    }
};
    
const TablePrograms = (props) => {
    const [showCreateProgram, setShowCreateProgram] = useState(false);
    const auth = useAuth(); 
   
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
    const [ listPrograms, setListPrograms] = useState([]);
    const [totalPrograms, setTotalPrograms] = useState(0); 
    const [totalPages, setTotalPages] = useState(0);
   

    useEffect(() => {
        if (!auth.isLoanding  && !!auth.userData ) {
        console.log(auth);
        console.log(auth.userData?.profile);
        getPrograms(1,auth.userData?.profile.preferred_username);
    }}, [auth.isLoading,auth.userData])
    
    const getPrograms = async (page,username) => { 
        let res = await fetchAllProgram(page,username);
        
        if(res && res.program_list) { 
            console.log(res)
            setTotalPrograms(res.total)
            setListPrograms(res.program_list)
            setTotalPages(res.total_pages); 
        }
    }
    const handlePageClick = (event) => { 
        console.log("event lib: ", event)
        getPrograms(+ event.selected + 1)
    }
    return (
        <>
        <nav className="p-3 bg-light">
       
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
                        <th scope="col">
                            <Form className="ClickAll" style={{ display: 'flex', justifyContent: 'center' }}>
                                <Form.Check
                                    type="checkbox"
                                    id="check-all"
                                    onChange={handleCheckAll} // Attach the event handler here
                                />
                            </Form>
                        </th>
                        <th scope="col">ID</th>
                        <th scope="col">Program Name</th>
                        <th scope="col">Create Date</th>
                        <th scope="col">Deadline</th>
                        <th scope="col">Status</th>
                    </tr>
                </thead>
                <tbody>
                    {listPrograms && listPrograms.length > 0 &&
                    listPrograms.map((item, index) => { 
                         return (
                    <tr key = {`programs-${index}`}>
                        <th>
                            <Form className="CheckOption" style={{ display: 'flex', justifyContent: 'center' }}>
                                <Form.Check type="checkbox" />
                            </Form>
                        </th>
                            
                        <td>{item.id}</td>
                        <td>{item.name}</td>
                        <td>{item.createTime}</td>
                        <td>{item.endTime}</td>
                        <td>{item.status}</td>
                    </tr>   
                          )   
                   })       
                  } 
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
        </>
    )
}
export default TablePrograms;