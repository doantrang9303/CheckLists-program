import React, { useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import CreateProgram from './CreateProgram';
import Form from 'react-bootstrap/Form';
import Dropdown from 'react-bootstrap/Dropdown';
import DropdownButton from 'react-bootstrap/DropdownButton';
import ProgramSerivce from './services/ProgramService';
import { useAuth } from 'oidc-react';
import { format } from 'date-fns'; // Import định dạng ngày tháng từ date-fns
import ReactPaginate from 'react-paginate';
import { Link } from 'react-router-dom';
import Swal from 'sweetalert2';


const TablePrograms = (props) => {
    const [showCreateProgram, setShowCreateProgram] = useState(false);
    const [refresh, setRefresh] = useState(false);
    const auth = useAuth();
    const [counter, setCounter] = useState(1); // Biến đếm cho ID
    const [startingId, setStartingId] = useState(1); // ID bắt đầu
    const formatDate = (dateString) => {
        return format(new Date(dateString), 'yyyy/MM/dd'); // Định dạng ngày tháng
    };

    // Function click event to delete//////////////////////////////////////////////////
    const [selectedPrograms, setSelectedPrograms] = useState([]);
    // Function to handle the click event of the "checkbox-all"
    const toggleProgramSelection = (programId) => {
        if (selectedPrograms.includes(programId)) {
            setSelectedPrograms(selectedPrograms.filter(id => id !== programId));
        } else {
            setSelectedPrograms([...selectedPrograms, programId]);
        }
    };
    const deleteSelectedPrograms = async () => {
        const confirmDelete = await Swal.fire({
            title: 'Are you sure?',
            text: 'You won\'t be able to revert this!',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        });
        if (confirmDelete.isConfirmed) {
            for (const programId of selectedPrograms) {
                await ProgramSerivce.deleteProgram(programId);
            }
            if (listPrograms.length === 1 && currentPage === 1) {
                setListPrograms([]);
                setTotalPrograms(0);
                setTotalPage(0);
            } else {
                getPrograms(currentPage, auth.userData?.profile.preferred_username);
            }
            setSelectedPrograms([]); // Clear selected programs after deletion
            Swal.fire(
                'Deleted!',
                'Your program has been deleted.',
                'success'
            );
        }
    };
    ///////////////////Delete//////////////////////////////
    const handleCheckAll = (event) => {
        const isChecked = event.target.checked;
        const updatedSelectedPrograms = isChecked ? listPrograms.map(program => program.id) : [];
        setSelectedPrograms(updatedSelectedPrograms);
    };
    
    const handleCreateProgramClick = () => {
        setShowCreateProgram(true);
    };

    const handleCloseCreateProgram = () => {
        setShowCreateProgram(false);
        // Refresh the data on the current page
        setCurrentPage(1);
        getPrograms(currentPage, auth.userData?.profile.preferred_username)

    };
    const [selectedOption, setSelectedOption] = useState(""); // State to track selected option

    const handleSelectChange = (event) => {
        setSelectedOption(event.target.value); // Update selected option
    };
    const [listPrograms, setListPrograms] = useState([]);
    const [totalPrograms, setTotalPrograms] = useState(0);
    const [totalPage, setTotalPage] = useState(0);
    const [currentPage, setCurrentPage] = useState(1); // Trang hiện tại
    /////////////////useEffect////////////////
    useEffect(() => {
        if (!auth.isLoading && !!auth.userData) {
            console.log(auth);
            console.log(auth.userData?.profile);
            getPrograms(currentPage, auth.userData?.profile.preferred_username);

        }
    }, [auth.isLoading, auth.userData, refresh])

    useEffect(() => {
        if (listPrograms.length === 0 && currentPage > 1) {
            const previousPage = currentPage - 1;
            getPrograms(previousPage, auth.userData?.profile.preferred_username);
            setCurrentPage(previousPage);
        }
    }, [listPrograms, currentPage, auth.userData]);
    /////////////////useEffect////////////////
    const getPrograms = async (page, username) => {

        let res = await ProgramSerivce.fetchAllProgram(page, username);
        setTotalPrograms(res.total)
        setListPrograms(res.program_list)
        setTotalPage(res.total_page);
    }
    const handlePageClick = (event) => {
        console.log("event lib: ", event)
        const nextPage = + event.selected + 1;
        setCurrentPage(nextPage); // Update currentPage state
        getPrograms(nextPage, auth.userData?.profile.preferred_username);
        const newStartingId = (nextPage - 1) * 10 + 1; // Tính toán ID bắt đầu của trang mới
        setStartingId(newStartingId); // Cập nhật ID bắt đầu
        setCounter(newStartingId); // Cập nhật counter
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
                        <DropdownButton id="dropdown-basic-button" title="Status" className=" outline-dark    ms-2"
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
                            onClick={deleteSelectedPrograms}
                        >
                            Delete
                        </Button>
                    </li>

                </ul>


                {showCreateProgram && <CreateProgram onClose={handleCloseCreateProgram} />}
                {listPrograms.length === 0 && currentPage === 1 ? (
                    <p>There are no Program</p>
                ) :
                    <>
                        <table className="table caption-top bg-white rounded">
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
                                            <tr key={`programs-${index}`}>
                                                <th>
                                                    <Form className="CheckOption" style={{ display: 'flex', justifyContent: 'center' }}>
                                                        <Form.Check
                                                            type="checkbox"
                                                            onChange={() => toggleProgramSelection(item.id)}
                                                            checked={selectedPrograms.includes(item.id)}
                                                        />
                                                    </Form>
                                                </th>
                                                <td><Link to={`/TaskPage/${item.id}`} className='nav-link'>{item.name}</Link></td>
                                                <td><Link to={`/TaskPage/${item.id}`} className='nav-link'>{formatDate(item.create_time)}</Link></td>
                                                <td><Link to={`/TaskPage/${item.id}`} className='nav-link'>{formatDate(item.end_time)}</Link></td>
                                                <td><Link to={`/TaskPage/${item.id}`} className='nav-link'>{item.status}</Link></td>
                                            </tr>
                                        )
                                    })
                                }
                            </tbody>
                        </table>
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
                            containerClassName="pagination justify-content-center" // Thêm lớp justify-content-center để căn giữa
                            activeClassName="active"
                        />
                    </>
                }
            </nav >
        </>
    )
}
export default TablePrograms;
