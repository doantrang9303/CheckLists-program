// TaskPage.js
import React, { useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Dropdown from 'react-bootstrap/Dropdown';
import DropdownButton from 'react-bootstrap/DropdownButton';
import Form from 'react-bootstrap/Form';
import TaskService from '../services/TaskService';
import { Link, useParams } from 'react-router-dom'; // Import useParams hook
import { format } from 'date-fns';
import { useAuth } from 'oidc-react';
import CreateTask from './CreateTask';
import ReactPaginate from 'react-paginate';

const TaskPage = (props) => {
    const [showCreateTask, setShowCreateTask] = useState(false);
    const [counter, setCounter] = useState(1); // Biến đếm cho ID
    const [startingId, setStartingId] = useState(1); // ID bắt đầu
    const [listTasks, setListTasks] = useState([]);
    const [totalTasks, setTotalTasks] = useState(0);
    const [totalPage, setTotalPage] = useState(0);
    const [currentPage, setCurrentPage] = useState(1); // Trang hiện tại
    const formatDate = (dateString) => {
        return format(new Date(dateString), 'yyyy/MM/dd'); // Định dạng ngày tháng
    };
    const auth = useAuth();
    const [refresh, setRefresh] = useState(false);
    const { id } = useParams();

    const getTasks = async (page, id) => {
        let res = await TaskService.fetchAllTask(page, id);
        setTotalTasks(res.total)
        setListTasks(res.task_list)
        setTotalPage(res.total_page);

    }
    useEffect(() => {
        if (!auth.isLoading && !!auth.userData) {
            console.log(currentPage);
            getTasks(currentPage, id);
        }
    }, [auth.isLoading, auth.userData, refresh, id])
    ////////////////////////////////Delete Task///////////////////////////////////
    const handleCheckAll = (event) => {
        const checkboxes = document.querySelectorAll('.CheckOption input[type="checkbox"]');
        checkboxes.forEach((checkbox) => {
            checkbox.checked = event.target.checked;
        });
    };

    const [selectedTasks, setSelectedTasks] = useState([]);
    // Function to handle the click event of the "checkbox-all"
    const toggleTaskSelection = (taskId) => {
        if (selectedTasks.includes(taskId)) {
            setSelectedTasks(selectedTasks.filter(id => id !== taskId));
        } else {
            setSelectedTasks([...selectedTasks, taskId]);
        }
    };
    const deleteSelectedTasks = async () => {
        for (const taskId of selectedTasks) {
            await TaskService.deleteTask(taskId);
        }
        if (listTasks.length === 1 && currentPage === 1) {
            setListTasks([]);
            setTotalTasks(0);
            setTotalPage(0);
        } else {
            getTasks(currentPage, id);
        }
        setSelectedTasks([]); // Clear selected programs after deletion
    };
    //////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////Create Task///////////////////////////////////
    const handleCreateTaskClick = () => {
        setShowCreateTask(true);
    };

    const handleCloseCreateTask = () => {
        setShowCreateTask(false);
        // Refresh the data on the current page
        setCurrentPage(1);
        getTasks(currentPage, id)
    };
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////Paging//////////////////////////////////////
    const handlePageClick = (event) => {
        console.log("event lib: ", event)
        const nextPage = + event.selected + 1;
        setCurrentPage(nextPage); // Update currentPage state
        getTasks(nextPage, id);
        const newStartingId = (nextPage - 1) * 10 + 1; // Tính toán ID bắt đầu của trang mới
        setStartingId(newStartingId); // Cập nhật ID bắt đầu
        setCounter(newStartingId); // Cập nhật counter
    }
    ////////////////////////////////////////////////////////////////////////////////
    return (
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
                        onClick={handleCreateTaskClick}
                    >
                        Create new
                    </Button>
                </li>
                <li style={{ display: 'inline-block' }}>
                    <Button style={{ width: '125px' }}
                        type="button"
                        className="text-center btn btn-custom1 btn-outline-dark btn-lg fs-6 ms-2"
                        onClick={deleteSelectedTasks}
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
            {showCreateTask && <CreateTask onClose={handleCloseCreateTask} />}
            {listTasks.length === 0 && currentPage === 1 ? (
                <p>There are no Task</p>
            ) :
                <>
                    <table className="table caption-top bg-white rounded">

                        <thead>
                            <tr>
                                <th colSpan="3" style={{ textAlign: 'left', fontSize: '25px', paddingLeft: '3rem' }}>   Program: Hire Student</th>
                                <th colSpan="2" style={{ textAlign: 'right' }}>Deadline: 2024/04/02</th>
                            </tr>
                            <tr>
                                <th colSpan="1">
                                    <Form className="ClickAll" style={{ display: 'flex', justifyContent: 'center' }}>
                                        <Form.Check
                                            type="checkbox"
                                            id="check-all"
                                            onChange={handleCheckAll}
                                        />
                                    </Form>
                                </th>
                                <th colSpan="1" style={{ textAlign: 'center' }}>Task</th>
                                <th colSpan="1" style={{ textAlign: 'center' }}>Create Date</th>
                                <th colSpan="1" style={{ textAlign: 'center' }}>Deadline</th>
                                <th colSpan="1" style={{ textAlign: 'center' }}>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            {listTasks && listTasks.length > 0 &&
                                listTasks.map((item, index) => {
                                    return (
                                        <tr key={`tasks-${index}`}>
                                            <th>
                                                <Form className="CheckOption" style={{ display: 'flex', justifyContent: 'center' }}>
                                                    <Form.Check
                                                        type="checkbox"
                                                        onChange={() => toggleTaskSelection(item.id)}
                                                        checked={selectedTasks.includes(item.id)}
                                                    />
                                                </Form>
                                            </th>
                                            <td style={{ textAlign: 'center' }} >{item.task_name}</td> {/* Thay thế dòng này */}
                                            <td style={{ textAlign: 'center' }}>{formatDate(item.create_time)}</td>
                                            <td style={{ textAlign: 'center' }}>{formatDate(item.end_time)}</td>
                                            <td style={{ textAlign: 'center' }}>{item.status}</td>
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
            <li style={{ display: 'flex', justifyContent: 'center' }}>
                <Link to="/" style={{ textDecoration: 'none', width: '150px' }}>
                    <Button
                        type="button"
                        className="text-center btn btn-custom1 btn-outline-dark btn-lg fs-6 ms-2 mb-2"
                        style={{ width: '100%', cursor: 'pointer' }}
                    >
                        Go back
                    </Button>
                </Link>
            </li>
        </nav>
    );
}

export default TaskPage;