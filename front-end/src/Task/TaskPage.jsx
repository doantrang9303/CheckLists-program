// TaskPage.js
import React, { useEffect, useState } from "react";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import TaskService from "../services/TaskService";
import { useParams } from "react-router-dom"; // Import useParams hook
import { format } from "date-fns";
import { useAuth } from "oidc-react";
import CreateTask from "./CreateTask";
import ReactPaginate from "react-paginate";
import EditTask from "./EditTask";
import Swal from "sweetalert2";
import { debounce } from "lodash";
import { CSVLink, CSVDownload } from "react-csv";
import { toast } from "react-toastify";
import "./TaskPage.css";
import * as XLSX from "xlsx";
import { ProgressBar } from "react-bootstrap";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import "react-toastify/dist/ReactToastify.css";
const TaskPage = (props) => {
    const [showCreateTask, setShowCreateTask] = useState(false);
    const [showEditTask, setShowEditTask] = useState(false);
    const [listTasks, setListTasks] = useState([]);
    const [totalTasks, setTotalTasks] = useState(0);
    const [totalPage, setTotalPage] = useState(0);
    const [currentPage, setCurrentPage] = useState(1); // Trang hiện tại
    const [selectedTask, setSelectedTask] = useState(null);
    const formatDate = (dateString) => {
        return format(new Date(dateString), "yyyy/MM/dd"); // Định dạng ngày tháng
    };
    const auth = useAuth();
    const { id, name, endate } = useParams();

    const [isCheckAllChecked, setIsCheckAllChecked] = useState(false);

    const getTasks = async (page, id) => {
        let res = await TaskService.fetchAllTask(page, id);
        setTotalTasks(res.total);
        setListTasks(res.task_list);
        setTotalPage(res.total_page);
    };
    useEffect(() => {
        if (!auth.isLoading && !!auth.userData) {
            console.log(currentPage);
            getTasks(currentPage, id);
        }
    }, [auth.isLoading, auth.userData, id]);
    useEffect(() => {
        const sock = new SockJS("http://localhost:9292/ws");
        const client = Stomp.over(sock);
        client.connect({}, () => {
            console.log("Connected to web socket!");
            client.subscribe("/topic/progress", (message) => {
                const data = JSON.parse(message.body);
                setImportProgress(
                    Math.floor((data.savedCount / data.totalCount) * 100)
                );
            });
            return () => {
                client.abort();
            };
        });
    }, []);
    ////////////////////////////////Delete Task///////////////////////////////////
    const handleCheckAll = (event) => {
        const isChecked = event.target.checked;
        setIsCheckAllChecked(isChecked); // Cập nhật trạng thái của checkbox master

        const updatedSelectedTasks = isChecked
            ? listTasks.map((task) => task.id)
            : [];
        setSelectedTasks(updatedSelectedTasks); // Cập nhật trạng thái của tất cả các checkbox con
    };

    const [selectedTasks, setSelectedTasks] = useState([]);
    const [isDeleteButtonEnabled, setIsDeleteButtonEnabled] = useState(false);
    // Function to handle the click event of the "checkbox-all"
    const toggleTaskSelection = (taskId) => {
        if (selectedTasks.includes(taskId)) {
            setSelectedTasks(selectedTasks.filter((id) => id !== taskId));
        } else {
            setSelectedTasks([...selectedTasks, taskId]);
        }
    };
    const deleteSelectedTasks = async () => {
        // Hàm này sẽ hiển thị hộp thoại xác nhận khi người dùng nhấn nút "Delete"
        const confirmDelete = await Swal.fire({
            title: "Are you sure?",
            text: "You won't be able to revert this!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Yes, delete it!",
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
            Swal.fire("Deleted!", "Your Task has been deleted.", "success");
        }
    };
    useEffect(() => {
        setIsDeleteButtonEnabled(selectedTasks.length > 0);
    }, [selectedTasks]);

    ////////////////////////////////Create Task///////////////////////////////////
    const handleCreateTaskClick = () => {
        setShowCreateTask(true);
    };

    const handleCloseCreateTask = () => {
        setShowCreateTask(false);
        // Refresh the data on the current page
        setCurrentPage(1);
        getTasks(currentPage, id);
    };
    ////////////////////////////////Paging//////////////////////////////////////
    const [search, setSearch] = useState("");
    const [selectedStatus, setSelectedStatus] = useState("");

    // Thêm state mới để lưu trữ trang hiện tại của danh sách chương trình khi lọc theo trạng thái
    const [currentPageFiltered, setCurrentPageFiltered] = useState(1);

    // Hàm xử lý khi thay đổi giá trị của trường nhập liệu "Filter by Name-DeadLine"
    const handleFilterByName = debounce(async (event) => {
        const { value } = event.target;
        setSearch(value); // Cập nhật giá trị của state search với giá trị mới từ trường nhập liệu
        setCurrentPageFiltered(1); // Reset trang về 1 khi thay đổi giá trị của trường nhập liệu
        try {
            // Gọi hàm lấy dữ liệu chương trình với trang, tên người dùng, và tên chương trình
            const res = await TaskService.filterTaskByName(
                value,
                id,
                currentPage
            );
            if (res && res.task_list) {
                setTotalTasks(res.total);
                setListTasks(res.task_list);
                setTotalPage(res.total_page);
            }
        } catch (error) {
            console.error("Error filtering programs by name:", error);
        }
    }, 200);

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

    //------------Export---------------------------

    const handleExportExcel = async () => {
        try {
            let allTasks = []; // Array to store tasks from all pages

            // Fetch tasks from each page and concatenate them to allTasks array
            for (let page = 1; page <= totalPage; page++) {
                const response = await TaskService.fetchAllTask(page, id);
                allTasks = allTasks.concat(response.task_list);
            }

            // Convert tasks to exportable format
            let result = allTasks.map((task) => ({
                name: task.task_name,
                status: task.status,
                create_time: formatDate(task.create_time),
                end_time: formatDate(task.end_time),
            }));

            // Create Excel workbook and sheet
            const ws = XLSX.utils.json_to_sheet(result);
            const wb = XLSX.utils.book_new();
            XLSX.utils.book_append_sheet(wb, ws, "Tasks");

            // Convert workbook to array buffer
            const wbout = XLSX.write(wb, { bookType: "xlsx", type: "array" });

            // Create Blob from array buffer
            const blob = new Blob([wbout], {
                type: "application/octet-stream",
            });

            // Create URL for Blob
            const url = URL.createObjectURL(blob);

            // Create anchor element to trigger download
            const a = document.createElement("a");
            a.href = url;
            a.download = `${name}.xlsx`;

            // Trigger click on anchor element
            a.click();

            // Cleanup
            setTimeout(() => {
                URL.revokeObjectURL(url);
            }, 0);
        } catch (error) {
            console.error("Error exporting tasks:", error);
            toast.error("Failed to export data. Please try again.");
        }
    };

    const [importProgress, setImportProgress] = useState(0);
    const [isImporting, setIsImporting] = useState(false); // State để kiểm soát việc hiển thị ProgressBar
    const handleImportExcel = async (event) => {
        if (event.target && event.target.files && event.target.files[0]) {
            const file = event.target.files[0];
            if (
                file.type !==
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            ) {
                toast.error("Only accept Excel files (.xlsx) ...");
                return;
            }

            try {
                const reader = new FileReader();
                reader.onload = async (e) => {
                    const data = new Uint8Array(e.target.result);
                    const workbook = XLSX.read(data, { type: "array" });

                    // Get list of all sheets in the workbook
                    const sheetNames = workbook.SheetNames;

                    // Get data from the first sheet
                    const firstSheet = workbook.Sheets[sheetNames[0]];

                    // Convert data from sheet to array of objects
                    const excelData = XLSX.utils.sheet_to_json(firstSheet);
                    // Check if create_time field is empty in all rows
                    const isCreateTimeEmpty = excelData.every(
                        (row) => !row.create_time
                    );

                    if (!isCreateTimeEmpty) {
                        toast.error(
                            "Trường create_time phải để trống trong tất cả các hàng."
                        );
                        getTasks(currentPage, id);
                        return;
                    }
                    setIsImporting(true);
                    setImportProgress(0);
                    const response = await TaskService.importFile(file, id);
                    console.log("Data imported successfully:", response.data);
                    if (response.savedCount === 0) {
                        toast.error(
                            "Import dữ liệu không thành công vì end_time Task đã vượt quá end_time Program. Hãy kiểm tra lại!!!!"
                        );
                        getTasks(currentPage, id);
                    } else if (response.savedCount < response.totalCount) {
                        toast.warning(
                            "Import dữ liệu thành công nhưng 1 trong số đó có endTime Task vượt quá endTime Program. Hãy kiểm tra lại!!!! "
                        );
                        getTasks(currentPage, id);
                    } else {
                        toast.success("Import dữ liệu thành công");
                        getTasks(currentPage, id);
                    }
                    setTimeout(() => {
                        setIsImporting(false);
                    }, 750);
                };

                reader.readAsArrayBuffer(file);
            } catch (error) {
                console.error("Failed to import data:", error);
                toast.error("Failed to import data. Please try again.");
            }
        }
        // Clear input value after import completes
        event.target.value = null;
    };

    ///////////////////////////////////Edit Task////////////////////////////////////
    const handleEditClick = (task) => {
        setSelectedTask(task); // Set selected task when a row is clicked
        setShowEditTask(true); // Open edit modal
    };
    const handleCloseEditTask = () => {
        setShowEditTask(false); // Hide edit modal
        setCurrentPage(1);
        getTasks(currentPage, id);
    };
    return (
        <nav className="p-3 bg-light">
            <ul style={{ display: "flex", whiteSpace: "nowrap" }}>
                <li style={{ display: "inline-block" }}>
                    <div>
                        <input
                            className="form-control"
                            placeholder="Filter by Name-DeadLine"
                            onChange={debounce(handleFilterByName, 200)} // Sử dụng hàm handleSearchChange
                        />
                    </div>
                </li>

                <li style={{ display: "inline-block", marginLeft: "10px" }}>
                    <Form.Select
                        aria-label="Default select example"
                        className="status"
                        onChange={handleSelectedStatus}
                    >
                        <option value="">Filter by Status</option>
                        <option value="In_progress">IN_PROGRESS</option>
                        <option value="Completed">COMPLETED</option>
                    </Form.Select>
                </li>
                <li style={{ display: "inline-block", marginLeft: "auto" }}>
                    <Button
                        style={{ width: "125px", color: "white" }}
                        type="button"
                        className="btn btn-info"
                        onClick={handleCreateTaskClick}
                    >
                        Create Task
                    </Button>
                </li>
                <li style={{ display: "inline-block", margin: "0 10px" }}>
                    <Button
                        style={{ width: "125px" }}
                        type="button"
                        className="btn btn-danger"
                        onClick={deleteSelectedTasks}
                        disabled={!isDeleteButtonEnabled}
                    >
                        Delete
                    </Button>
                </li>
                <li style={{ display: "inline-block", marginRight: "10px" }}>
                    <div>
                        <Button
                            style={{ width: "125px" }}
                            type="button"
                            className="btn btn-primary"
                            onClick={handleExportExcel}
                        >
                            <i className="fa-solid fa-file-arrow-down"></i>{" "}
                            Export
                        </Button>

                        <label
                            htmlFor="fileInput"
                            className="btn btn-success"
                            style={{ marginLeft: "10px" }}
                        >
                            Import
                        </label>
                        <input
                            id="fileInput"
                            type="file"
                            hidden
                            onChange={(event) => handleImportExcel(event, id)}
                        />
                        {isImporting && (
                            <ProgressBar
                                animated
                                now={importProgress}
                                label={`${importProgress}%`}
                                style={{ marginTop: "10px" }}
                            />
                        )}
                    </div>
                </li>
            </ul>
            {showCreateTask && <CreateTask onClose={handleCloseCreateTask} />}
            {showEditTask && (
                <EditTask task={selectedTask} onClose={handleCloseEditTask} />
            )}
            <tr>
                <th
                    colSpan="3"
                    style={{ fontSize: "25px", paddingLeft: "3rem" }}
                >
                    {" "}
                    Program: {name}{" "}
                </th>
                <th colSpan="2" style={{ paddingLeft: "2rem" }}>
                    Deadline: {endate}
                </th>
            </tr>
            {listTasks.length === 0 && currentPage === 1 ? (
                <h3 style={{ textAlign: "center" }}>There are no Task</h3>
            ) : (
                <>
                    <div className="table-wrapper">
                        <table className="table caption-top bg-white rounded table-striped">
                            <thead>
                                <tr>
                                    <th colSpan="1">
                                        <Form
                                            className="ClickAll"
                                            style={{
                                                display: "flex",
                                                justifyContent: "center",
                                            }}
                                        >
                                            <Form.Check
                                                type="checkbox"
                                                id="check-all"
                                                onChange={handleCheckAll}
                                            />
                                        </Form>
                                    </th>
                                    <th
                                        colSpan="1"
                                        style={{ textAlign: "center" }}
                                    >
                                        Task
                                    </th>
                                    <th
                                        colSpan="1"
                                        style={{ textAlign: "center" }}
                                    >
                                        Create Date
                                    </th>
                                    <th
                                        colSpan="1"
                                        style={{ textAlign: "center" }}
                                    >
                                        Deadline
                                    </th>
                                    <th
                                        colSpan="1"
                                        style={{ textAlign: "center" }}
                                    >
                                        Status
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                {listTasks.map((item) => (
                                    <tr key={item.id}>
                                        <th>
                                            <Form
                                                className="CheckOption"
                                                style={{
                                                    display: "flex",
                                                    justifyContent: "center",
                                                }}
                                            >
                                                <Form.Check
                                                    type="checkbox"
                                                    onChange={() =>
                                                        toggleTaskSelection(
                                                            item.id
                                                        )
                                                    }
                                                    checked={selectedTasks.includes(
                                                        item.id
                                                    )}
                                                />
                                            </Form>
                                        </th>
                                        <td
                                            style={{ textAlign: "center" }}
                                            onClick={() =>
                                                handleEditClick(item)
                                            }
                                        >
                                            {item.task_name}
                                        </td>{" "}
                                        {/* Thay thế dòng này */}
                                        <td
                                            style={{ textAlign: "center" }}
                                            onClick={() =>
                                                handleEditClick(item)
                                            }
                                        >
                                            {formatDate(item.create_time)}
                                        </td>
                                        <td
                                            style={{ textAlign: "center" }}
                                            onClick={() =>
                                                handleEditClick(item)
                                            }
                                        >
                                            {formatDate(item.end_time)}
                                        </td>
                                        <td
                                            style={{
                                                textAlign: "center",
                                                color:
                                                    item.status ===
                                                    "MISS_DEADLINE"
                                                        ? "red"
                                                        : item.status ===
                                                          "IN_PROGRESS"
                                                        ? "orange"
                                                        : "green",
                                            }}
                                            onClick={() =>
                                                handleEditClick(item)
                                            }
                                        >
                                            {item.status}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                    <div className="pagination-container">
                        <ReactPaginate
                            breakLabel="..."
                            nextLabel="next >"
                            onPageChange={(selectedPage) =>
                                handlePageChangeFiltered(selectedPage.selected)
                            }
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
                    </div>
                </>
            )}
        </nav>
    );
};

export default TaskPage;
