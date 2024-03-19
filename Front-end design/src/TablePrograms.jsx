import React, { useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import CreateProgram from './CreateProgram';
import axios from 'axios';
import Pagination from 'react-bootstrap/Pagination';
import Form from 'react-bootstrap/Form';
import Dropdown from 'react-bootstrap/Dropdown';
import DropdownButton from 'react-bootstrap/DropdownButton';
<<<<<<< Updated upstream
import ProgramSerivce from './services/ProgramService';
import { useAuth } from 'oidc-react';  
=======
import ProgramService from './services/ProgramService';
import { useAuth } from 'oidc-react';
>>>>>>> Stashed changes
import { format } from 'date-fns'; // Import định dạng ngày tháng từ date-fns
import ReactPaginate from 'react-paginate';
import { debounce } from 'lodash';

    
const TablePrograms = (props) => {
    const [showCreateProgram, setShowCreateProgram] = useState(false);
    const [refresh, setRefresh] = useState(false);
    const auth = useAuth(); 
    const [counter, setCounter] = useState(1); // Biến đếm cho ID
    const [startingId, setStartingId] = useState(1); // ID bắt đầu
    const formatDate = (dateString) => {
        return format(new Date(dateString), 'yyyy/MM/dd'); // Định dạng ngày tháng
    };
   

    // Function to handle the click event of the "checkbox-all"
<<<<<<< Updated upstream
=======
    const toggleProgramSelection = (programId) => {
        if (selectedPrograms.includes(programId)) {
            setSelectedPrograms(selectedPrograms.filter(id => id !== programId));
        } else {
            setSelectedPrograms([...selectedPrograms, programId]);
        }
    };
    const deleteSelectedPrograms = async () => {
        for (const programId of selectedPrograms) {
            await ProgramService.deleteProgram(programId);
            
        }
        if (listPrograms.length === 1 && currentPage > 1) {
            getPrograms(currentPage - 1,  auth.userData?.profile.preferred_username);
        } else if (listPrograms.length === 1 && currentPage === 1) {
            setListPrograms([]); 
            setTotalPrograms(0); 
            setTotalPage(0);
        } else{
        getPrograms(currentPage, auth.userData?.profile.preferred_username);}
        setSelectedPrograms([]); // Clear selected programs after deletion
    };


    const filterByStatus = async () => {
        try {
            await getPrograms(1, auth.userData?.profile.preferred_username, selectedStatus);
        } catch (error) {
            console.error("Error filtering programs by status:", error);
        }
    };
    
>>>>>>> Stashed changes
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
    const [totalPage, setTotalPage] = useState(0);
    const [currentPage, setCurrentPage] = useState(1); // Trang hiện tại

    useEffect(() => {
<<<<<<< Updated upstream
        if (!auth.isLoading  && !!auth.userData ) {
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
            setTotalPage(res.total_page); 
            setCurrentPage(page); // Cập nhật trang hiện tại
        }
    }
    const handlePageClick = (event) => { 
=======
        if (!auth.isLoading && !!auth.userData) {            
            console.log(auth);
            console.log(auth.userData?.profile);
            getPrograms(1, auth.userData?.profile.preferred_username);
        }
    }, [auth.isLoading, auth.userData, refresh])

    const getPrograms = async (page, username, status = "") => {
        let res;
        if (status !== "") {
            res = await ProgramService.filterProgramByStatus( status,username);
        } else {
            res = await ProgramService.fetchAllProgram(page, username);
        }
        if (res && res.program_list) {
            setTotalPrograms(res.total)
            setListPrograms(res.program_list)
            setTotalPage(res.total_page);
        }
    };
    
    const handlePageClick = (event) => {
>>>>>>> Stashed changes
        console.log("event lib: ", event)
        const nextPage = + event.selected + 1;
        getPrograms(nextPage, auth.userData?.profile.preferred_username);
        const newStartingId = (nextPage - 1) * 10 + 1; // Tính toán ID bắt đầu của trang mới
        setStartingId(newStartingId); // Cập nhật ID bắt đầu
        setCounter(newStartingId); // Cập nhật counter
    }

   
    const [search, setSearch] = useState('');
    const [selectedStatus, setSelectedStatus] = useState(''); // State để lưu trữ trạng thái được chọn
    const handleSelectedStatus = (event) => {
        setSelectedStatus(event.target.value); // Cập nhật trạng thái được chọn
    };
    console.log(search)
    return (
        <>
<<<<<<< Updated upstream
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
                        <th scope="col" href="/TaskPage">Program Name</th>
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
                            
                        <td>{counter + index}</td> {/* ID được tự động tăng */}
                        <td>{item.name}</td>
                        <td>{formatDate(item.create_time)}</td>
                        <td>{formatDate(item.end_time)}</td>
                        <td>{item.status}</td>
                    </tr>   
                          )   
                   })       
                  } 
                </tbody>
            </table>
            <>
            {/* <div className='pagination-container'> */}
            <ReactPaginate 
                breakLabel="..."
                nextLabel="next >"
                onPageChange={handlePageClick}
                pageRangeDisplayed={5}
                pageCount={totalPage}
                previousLabel="< previous"
                
                pageClassName="page-item"
                pageLinkClassName="page-link"
                previousClassName="page-item"
                previousLinkClassName="page-link"
                nextClassName="page-item"
                nextLinkClassName="page-link"
                breakClassName="page-item"
                breakLinkClassName="page-link"
                containerClassName="pagination"
                activeClassName="active"
            />
            {/* </div> */}
            </>
        </nav >
=======
            <nav className="p-3 bg-light">

                <ul style={{ display: 'flex', whiteSpace: 'nowrap' }}>
                    <li style={{ display: 'inline-block' }}>
                    <div>
                        <input
                         className='form-control' 
                        placeholder='Search Program by Name'
                   
                        onChange={debounce((event) => setSearch(event.target.value),200)} 
                        />
                        
                    </div>
                    </li>
                    <li style={{ display: 'inline-block' }}>                     
                    <select name="status" className=" outline-dark ms-2" onChange={handleSelectedStatus}
                       onClick={filterByStatus}>
                    <option value="">STATUS</option>
                    <option value="in_progress">IN_PROGRESS</option>
                    <option value="Completed">Completed</option>
                </select>
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
                            onClick={deleteSelectedPrograms}
                        >
                            Delete
                        </Button>
                    </li>

                </ul>


                {showCreateProgram && <CreateProgram onClose={handleCloseCreateProgram} />}
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
                            {/* <th scope="col">ID</th> */}
                            <th scope="col">Program Name</th>
                            <th scope="col">Create Date</th>
                            <th scope="col">Deadline</th>
                            <th scope="col">Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        {listPrograms && listPrograms.length > 0 &&
                            listPrograms.filter((item)=>{
                                return search.toLowerCase() === '' ? item : (item.name.
                                toLowerCase().includes(search));
                            }).map((item) => {
                                return (
                                    <tr key={item.id}>
                                        <th>
                                            <Form className="CheckOption" style={{ display: 'flex', justifyContent: 'center' }}>
                                                <Form.Check
                                                    type="checkbox"
                                                    onChange={() => toggleProgramSelection(item.id)} // Toggle program selection
                                                    checked={selectedPrograms.includes(item.id)} // Check if program is selected
                                                />
                                            </Form>
                                        </th>
                                        {/* <td>{counter + index}</td> ID được tự động tăng */}
                                        <td>{item.name}</td>
                                        <td>{formatDate(item.create_time)}</td>
                                        <td>{formatDate(item.end_time)}</td>
                                        <td>{item.status}</td>
                                    </tr>
                                )
                            })
                        }
                    </tbody>
                </table>
                <>
                    {/* <div className='pagination-container'> */}
                    <ReactPaginate
                        breakLabel="..."
                        nextLabel="next >"
                        onPageChange={handlePageClick}
                        pageRangeDisplayed={5}
                        pageCount={totalPage}
                        previousLabel="< previous"

                        pageClassName="page-item"
                        pageLinkClassName="page-link"
                        previousClassName="page-item"
                        previousLinkClassName="page-link"
                        nextClassName="page-item"
                        nextLinkClassName="page-link"
                        breakClassName="page-item"
                        breakLinkClassName="page-link"
                        containerClassName="pagination"
                        activeClassName="active"
                    />
                    {/* </div> */}
                </>
            </nav >
>>>>>>> Stashed changes
        </>
    )
}
export default TablePrograms;

//18/3/2024