import React, { useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import CreateProgram from './CreateProgram';
import axios from 'axios';
import Pagination from 'react-bootstrap/Pagination';
import Form from 'react-bootstrap/Form';
import Dropdown from 'react-bootstrap/Dropdown';
import DropdownButton from 'react-bootstrap/DropdownButton';
import ProgramSerivce from './services/ProgramService';
import { useAuth } from 'oidc-react';  


    
const TablePrograms = (props) => {
    const [showCreateProgram, setShowCreateProgram] = useState(false);
    const [refresh, setRefresh] = useState(false);
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
        setRefresh(prev => !prev);
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
    }}, [auth.isLoading,auth.userData,refresh])
    
    const getPrograms = async (page,username) => { 
        let res = await ProgramSerivce.fetchAllProgram(page,username);
        
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
                      
                    >
                        Name Program
                    </Button>
                </li>
                <li style={{ display: 'inline-block' }}>
                    <Button style={{ width: '140px' }}
                        type="button"
                        className="text-center btn btn-custom1 btn-outline-dark btn-lg fs-6 ms-2"
                       
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
                    onClick={handleCreateProgramClick}
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
                        <td>{item.create_time}</td>
                        <td>{item.end_time}</td>
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