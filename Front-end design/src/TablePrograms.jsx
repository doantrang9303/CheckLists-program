import React, { useState, useEffect } from 'react';
import { Button, Form } from 'react-bootstrap';
import { format } from 'date-fns';
import { debounce } from 'lodash';
import ReactPaginate from 'react-paginate';
import ProgramService from './services/ProgramService';
import { useAuth } from 'oidc-react';
import './tablePrograms.css'
const TablePrograms = (props) => {
    const [currentPage, setCurrentPage] = useState(1);
    const [search, setSearch] = useState('');
    const [selectedStatus, setSelectedStatus] = useState('');
    const [listPrograms, setListPrograms] = useState([]);
    const [totalPrograms, setTotalPrograms] = useState(0);
    const [totalPage, setTotalPage] = useState(0);
    const [selectedPrograms, setSelectedPrograms] = useState([]);
    const auth = useAuth();

    useEffect(() => {
        if (!auth.isLoading && !!auth.userData) {
            console.log(auth);
            console.log(auth.userData?.profile);
            getPrograms(1, auth.userData?.profile.preferred_username);
        }
    }, [auth.isLoading, auth.userData]);

    const formatDate = (dateString) => {
        return format(new Date(dateString), 'yyyy/MM/dd');
    };

    const getPrograms = async (page, username, status = "") => {
        let res;
        if (status !== "") {
            res = await ProgramService.filterProgramByStatus(status, username);
        } else {
            res = await ProgramService.fetchAllProgram(page, username);
        }
        if (res && res.program_list) {
            setTotalPrograms(res.total);
            setListPrograms(res.program_list);
            setTotalPage(res.total_page);
        }
    };

    const handlePageClick = (event) => {
        if (!auth.isLoading && !!auth.userData) {
            console.log(auth);
            console.log(auth.userData?.profile);
            const nextPage = event.selected + 1;
            getPrograms(nextPage, auth.userData?.profile.preferred_username);
        }
    };

    const handleCreateProgramClick = () => {
        // Your logic for handling the creation of a new program
    };

    const deleteSelectedPrograms = async () => {
        for (const programId of selectedPrograms) {
            await ProgramService.deleteProgram(programId);
        }
        if (listPrograms.length === 1 && currentPage > 1) {
            getPrograms(currentPage - 1, auth.userData?.profile.preferred_username);
        } else if (listPrograms.length === 1 && currentPage === 1) {
            setListPrograms([]);
            setTotalPrograms(0);
            setTotalPage(0);
        } else {
            getPrograms(currentPage, auth.userData?.profile.preferred_username);
        }
        setSelectedPrograms([]);
    };

    const toggleProgramSelection = (programId) => {
        if (selectedPrograms.includes(programId)) {
            setSelectedPrograms(selectedPrograms.filter(id => id !== programId));
        } else {
            setSelectedPrograms([...selectedPrograms, programId]);
        }
    };

    const handleCheckAll = (event) => {
        const checkboxes = document.querySelectorAll('.CheckOption input[type="checkbox"]');
        checkboxes.forEach((checkbox) => {
            // Your logic for handling checkbox selection
        });
    };

    const filterByStatus = async () => {
        try {
            await getPrograms(1, auth.userData?.profile.preferred_username, selectedStatus);
        } catch (error) {
            console.error("Error filtering programs by status:", error);
        }
    };

    const handleSelectedStatus = (event) => {
        setSelectedStatus(event.target.value);
    };

    return (
        <>
            <nav className="p-3 bg-light">
                <ul style={{ display: 'flex', whiteSpace: 'nowrap' }}>
                    <li style={{ display: 'inline-block' }}>
                        <div>
                            <input
                                className='form-control'
                                placeholder='Search Program by Name'
                                onChange={debounce((event) => setSearch(event.target.value), 200)}
                            />
                        </div>
                    </li>
                    <li style={{ display: 'inline-block' }}>
                        {/* <select name="status" className="outline-dark ms-2">
                            <option value="">STATUS</option>
                            <option value="in_progress">IN_PROGRESS</option>
                            <option value="Completed">Completed</option>
                        </select> */}
                        <Form.Select aria-label="Default select example" className="status"  onChange={handleSelectedStatus} onClick={filterByStatus}>
                                <option value="">Filter by Status</option>
                                <option value="in_progress">IN_PROGRESS</option>
                                <option value="Completed">Completed</option>
                        </Form.Select>
                    </li>
                    <li style={{ display: 'inline-block', marginLeft: 'auto' }}>
                        <Button style={{ width: '125px' }} type="button" className="text-center btn btn-custom1 btn-outline-dark btn-lg fs-6 ms-2" onClick={handleCreateProgramClick}>
                            Create new
                        </Button>
                    </li>
                    <li style={{ display: 'inline-block' }}>
                        <Button style={{ width: '125px' }} type="button" className="text-center btn btn-custom1 btn-outline-dark btn-lg fs-6 ms-2" onClick={deleteSelectedPrograms}>
                            Delete
                        </Button>
                    </li>
                </ul>

                <table className="table caption-top bg-white rounded">
                    <thead>
                        <tr>
                            <th scope="col">
                                <Form className="ClickAll" style={{ display: 'flex', justifyContent: 'center' }}>
                                    <Form.Check
                                        type="checkbox"
                                        id="check-all"
                                        onChange={handleCheckAll}
                                    />
                                </Form>
                            </th>
                            <th scope="col">Program Name</th>
                            <th scope="col">Create Date</th>
                            <th scope="col">Deadline</th>
                            <th scope="col">Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        {listPrograms && listPrograms.length > 0 &&
                            listPrograms.filter((item) => {
                                return search.toLowerCase() === '' ? item : (item.name.toLowerCase().includes(search) ||
                                search.toLowerCase() === '' ? item : (formatDate(item.end_time).includes(search)) );
                            }).map((item) => {
                                return (
                                    <tr key={item.id}>
                                        <th>
                                            <Form className="CheckOption" style={{ display: 'flex', justifyContent: 'center' }}>
                                                <Form.Check
                                                    type="checkbox"
                                                    onChange={() => toggleProgramSelection(item.id)}
                                                    checked={selectedPrograms.includes(item.id)}
                                                />
                                            </Form>
                                        </th>
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
                </>
            </nav>
        </>
    )
}
export default TablePrograms;
