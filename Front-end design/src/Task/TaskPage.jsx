// TaskPage.js
import React, { useEffect, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Dropdown from 'react-bootstrap/Dropdown';
import DropdownButton from 'react-bootstrap/DropdownButton';
import Form from 'react-bootstrap/Form';
import TaskService from '../services/TaskService';
import { useParams } from 'react-router-dom'; // Import useParams hook
import { format } from 'date-fns';
import { useAuth } from 'oidc-react';
import CreateTask from './CreateTask';
import ReactPaginate from 'react-paginate';
import EditTask from './EditTask';
import Swal from 'sweetalert2';
import Papa from "papaparse";
import { debounce } from 'lodash';
import { CSVLink, CSVDownload } from 'react-csv';
import { toast } from 'react-toastify';

const TaskPage = (props) => {
    const [showCreateTask, setShowCreateTask] = useState(false);
    const [showEditTask, setShowEditTask] = useState(false);
    const [counter, setCounter] = useState(1); // Biến đếm cho ID
    const [startingId, setStartingId] = useState(1); // ID bắt đầu
    const [listTasks, setListTasks] = useState([]);
    const [totalTasks, setTotalTasks] = useState(0);
    const [totalPage, setTotalPage] = useState(0);
    const [currentPage, setCurrentPage] = useState(1); // Trang hiện tại
    const [selectedTask, setSelectedTask] = useState(null);
    const formatDate = (dateString) => {
        return format(new Date(dateString), 'yyyy/MM/dd'); // Định dạng ngày tháng
    };
    const auth = useAuth();
    const [refresh, setRefresh] = useState(false);
    const { id, name, endate } = useParams();

    const [isCheckAllChecked, setIsCheckAllChecked] = useState(false);

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
        const isChecked = event.target.checked;
        setIsCheckAllChecked(isChecked); // Cập nhật trạng thái của checkbox master

        const updatedSelectedTasks = isChecked ? listTasks.map(task => task.id) : [];
        setSelectedTasks(updatedSelectedTasks); // Cập nhật trạng thái của tất cả các checkbox con
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
        // Hàm này sẽ hiển thị hộp thoại xác nhận khi người dùng nhấn nút "Delete"
        const confirmDelete = await Swal.fire({
            title: 'Are you sure?',
            text: 'You won\'t be able to revert this!',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        });
        // Nếu người dùng chọn "OK", thì tiếp tục xóa các task được chọn
        if (confirmDelete.isConfirmed) {
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
            Swal.fire(
                'Deleted!',
                'Your Task has been deleted.',
                'success'
            );
        }
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
    const [search, setSearch] = useState('');
    const [selectedStatus, setSelectedStatus] = useState('');





    // Thêm state mới để lưu trữ trang hiện tại của danh sách chương trình khi lọc theo trạng thái
    const [currentPageFiltered, setCurrentPageFiltered] = useState(1);

    // Hàm mới để lấy dữ liệu chương trình với trang và trạng thái lọc
    const getTasksFiltered = async (page, id, status) => {
        try {
            let res;
            if (status != "") {
                res = await TaskService.filterTaskByStatus(status, id, page);
            } else {
                res = await TaskService.fetchAllTask(page, id);
            }
            if (res && res.task_list) {
                setTotalTasks(res.total);
                setListTasks(res.task_list);
                setTotalPage(res.total_page);
            }
        } catch (error) {
            console.error("Error filtering tasks by status:", error);
        }
    };

    // Hàm xử lý phân trang khi thay đổi trang hoặc trạng thái lọc
    const handlePageChangeFiltered = (selectedPage) => {
        setCurrentPageFiltered(selectedPage + 1); // Cập nhật trang hiện tại
        getTasksFiltered(selectedPage + 1, id, selectedStatus); // Gọi hàm để lấy dữ liệu mới
    };
    // Thêm sự kiện onChange cho dropdown lọc trạng thái để gọi hàm lọc chương trình mới
    const handleSelectedStatus = (event) => {
        setSelectedStatus(event.target.value);
        setCurrentPageFiltered(1); // Reset trang về 1 khi thay đổi trạng thái lọc
        getTasksFiltered(1, id, event.target.value); // Gọi hàm để lấy dữ liệu mới khi thay đổi trạng thái lọc
    };



    const [dataExport, setDataExport] = useState([]);
    const getTaskListExport = (event, done) => {
        let result = []
        if (listTasks && listTasks.length > 0) {
            result.push(["Task", "Create Time", "End Time", "Status"])
            listTasks.map((item, index) => {
                let arr = [];
                arr[0] = item.task_name;
                arr[1] = formatDate(item.create_time);
                arr[2] = formatDate(item.end_time);
                arr[3] = item.status;
                result.push(arr);
            })
            setDataExport(result);
            done();
        }
    }

    const handleImportCSV = (event) => {
        if (event.target && event.target.files && event.target.files[0]) {
            let file = event.target.files[0];
            if (file.type !== "text/csv") {
                toast.error("Only accept csv files ... ")
                return;
            }
            Papa.parse(file, {
                complete: function (results) {
                    console.log("Finished:", results.data)
                }
            })
        }

    }
    ////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////Edit Task////////////////////////////////////
    const handleEditClick = (task) => {
        setSelectedTask(task); // Set selected task when a row is clicked
        setShowEditTask(true); // Open edit modal
    };
    const handleCloseEditTask = () => {
        setShowEditTask(false); // Hide edit modal
        setCurrentPage(1);
        getTasks(currentPage, id)
    };
    ////////////////////////////////////////////////////////////////////////////////
    return (
        <nav className="p-3 bg-light">

            <ul style={{ display: 'flex', whiteSpace: 'nowrap' }}>
                <li style={{ display: 'inline-block' }}>
                    <div>
                        <input
                            className='form-control'
                            placeholder='Filter by Name-DeadLine'
                            onChange={debounce((event) => setSearch(event.target.value), 200)}
                        />
                    </div>
                </li>
                {/* <li style={{ display: 'inline-block' }}>
                <Button style={{ width: '140px' }}
                    type="button"
                    className="text-center btn btn-custom1 btn-outline-dark btn-lg fs-6 ms-2"
   
                >
                    Deadline
                </Button>
            </li> */}
                <li style={{ display: 'inline-block', marginLeft: "10px" }}>
                    <Form.Select aria-label="Default select example" className="status" onChange={handleSelectedStatus}>
                        <option value="">Filter by Status</option>
                        <option value="In_progress">IN_PROGRESS</option>
                        <option value="Completed">COMPLETED</option>
                    </Form.Select>
                </li>
                <li style={{ display: 'inline-block', marginLeft: 'auto' }}>
                    <Button style={{ width: '125px' , color: 'white' }}
                        type="button"
                        className="btn btn-info"
                        onClick={handleCreateTaskClick}
                    >
                        Create Task
                    </Button>
                </li>
                <li style={{ display: 'inline-block', margin: "0 10px" }}>
                    <Button style={{ width: '125px' }}
                        type="button"
                        className="btn btn-danger"
                        onClick={deleteSelectedTasks}
                    >
                        Delete
                    </Button>
                </li>
                <li style={{ display: 'inline-block', marginRight: "10px" }}>
                    <div>
                        <CSVLink
                            filename={"Task-list.csv"}
                            className="btn btn-primary"
                            data={dataExport}
                            asyncOnClick={true}
                            onClick={getTaskListExport}
                        ><i className="fa-solid fa-file-arrow-down"></i>Export
                        </CSVLink>

                        {/* <Button style={{ marginLeft:"10px" }}
                    type="button"
                    className="btn btn-success"                       
                >
                    Import
                </Button> */}
                        <label htmlFor='test' className='btn btn-success' style={{ marginLeft: "10px" }}>
                            Import
                        </label>
                        <input id="test" type="file" hidden
                            onChange={(event) => handleImportCSV(event)}
                        />

                    </div>
                </li>
            </ul>
            {showCreateTask && <CreateTask onClose={handleCloseCreateTask} />}
            {showEditTask && <EditTask task={selectedTask} onClose={handleCloseEditTask} />}
            <tr>
                <th colSpan="3" style={{ fontSize: '25px', paddingLeft: '3rem' }}>   Program: {name} </th>
                <th colSpan="2" style={{ paddingLeft: '2rem' }}>Deadline: {endate}</th>
            </tr>
            {listTasks.length === 0 && currentPage === 1 ? (
                <h3 style={{textAlign: "center"}} >There are no Task</h3>
            ) :
                <>
                    <table className="table table-hover caption-top bg-white rounded ">

                        <thead>
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
                                listTasks.filter((item) => {
                                    return search.toLowerCase() === '' ? item : (item.task_name.toLowerCase().includes(search) ||
                                        search.toLowerCase() === '' ? item : (formatDate(item.end_time).includes(search)));
                                }).map((item) => {
                                    return (
                                        <tr key={item.id}>
                                            <th>
                                                <Form className="CheckOption" style={{ display: 'flex', justifyContent: 'center' }}>
                                                    <Form.Check
                                                        type="checkbox"
                                                        onChange={() => toggleTaskSelection(item.id)}
                                                        checked={selectedTasks.includes(item.id)}
                                                    />
                                                </Form>
                                            </th>
                                            <td style={{ textAlign: 'center' }} onClick={() => handleEditClick(item)}>{item.task_name}</td> {/* Thay thế dòng này */}
                                            <td style={{ textAlign: 'center' }} onClick={() => handleEditClick(item)}>{formatDate(item.create_time)}</td>
                                            <td style={{ textAlign: 'center' }} onClick={() => handleEditClick(item)}>{formatDate(item.end_time)}</td>
                                            <td style={{ textAlign: 'center' }} onClick={() => handleEditClick(item)}>{item.status}</td>
                                        </tr>
                                    )
                                })
                            }


                        </tbody>
                    </table>
                    <ReactPaginate
                        breakLabel="..."
                        nextLabel="next >"
                        onPageChange={(selectedPage) => handlePageChangeFiltered(selectedPage.selected)}
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

        </nav>
    );
}

export default TaskPage;