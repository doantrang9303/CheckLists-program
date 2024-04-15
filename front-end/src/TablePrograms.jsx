import React, { useEffect, useState } from "react";
import Button from "react-bootstrap/Button";
import CreateProgram from "./CreateProgram";
import Form from "react-bootstrap/Form";
import ProgramSerivce from "./services/ProgramService";
import { useAuth } from "oidc-react";
import { format } from "date-fns";
import ReactPaginate from "react-paginate";
import { Link } from "react-router-dom";
import { debounce } from "lodash";
import Swal from "sweetalert2";
import "./TablePrograms.css";
import ProgramService from "./services/ProgramService";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
const TablePrograms = (props) => {
    const [showCreateProgram, setShowCreateProgram] = useState(false);
    const [showSuccessToast, setShowSuccessToast] = useState(false); // State để kiểm soát việc hiển thị toastify
    const auth = useAuth();
    const formatDate = (dateString) => {
        return format(new Date(dateString), "yyyy/MM/dd");
    };

    // Function click event to delete//////////////////////////////////////////////////
    const [selectedPrograms, setSelectedPrograms] = useState([]);
    const [isDeleteButtonEnabled, setIsDeleteButtonEnabled] = useState(false);
    // Function to handle the click event of the "checkbox-all"
    const toggleProgramSelection = (programId) => {
        if (selectedPrograms.includes(programId)) {
            setSelectedPrograms(
                selectedPrograms.filter((id) => id !== programId)
            );
        } else {
            setSelectedPrograms([...selectedPrograms, programId]);
        }
    };
    const deleteSelectedPrograms = async () => {
        const confirmDelete = await Swal.fire({
            title: "Are you sure?",
            text: "You won't be able to revert this!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Yes, delete it!",
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
                getPrograms(
                    currentPage,
                    auth.userData?.profile.preferred_username
                );
            }
            setSelectedPrograms([]); // Clear selected programs after deletion
            Swal.fire("Deleted!", "Your program has been deleted.", "success");
        }
    };
    useEffect(() => {
        setIsDeleteButtonEnabled(selectedPrograms.length > 0);
    }, [selectedPrograms]);
    ///////////////////Delete//////////////////////////////
    const handleCheckAll = (event) => {
        const isChecked = event.target.checked;
        const updatedSelectedPrograms = isChecked
            ? listPrograms.map((program) => program.id)
            : [];
        setSelectedPrograms(updatedSelectedPrograms);
    };

    const handleCreateProgramClick = () => {
        setShowCreateProgram(true);
    };

    // Xử lý sự kiện nhấp ra ngoài modal
    const handleOutsideClick = (event) => {
        if (event.target === document.querySelector(".modal-overlay")) {
            setShowCreateProgram(false);
            // Xóa dòng này để không hiển thị toastify khi thoát ra khỏi modal
        }
    };
    const handleCloseCreateProgram = async () => {
        setShowCreateProgram(false);
        // Refresh the data on the current page
        setCurrentPage(1);
        try {
            await getPrograms(
                currentPage,
                auth.userData?.profile.preferred_username
            );
            toast.success("Program created successfully!");
        } catch (error) {
            console.error("Failed to create program:", error);
            toast.error("Failed to create program. Please try again.");
        }
    };
    const [listPrograms, setListPrograms] = useState([]);
    const [totalPrograms, setTotalPrograms] = useState(0);
    const [totalPage, setTotalPage] = useState(0);
    const [currentPage, setCurrentPage] = useState(1);
    /////////////////useEffect////////////////
    useEffect(() => {
        if (!auth.isLoading && !!auth.userData) {
            getPrograms(currentPage, auth.userData?.profile.preferred_username);
        }
    }, [auth.isLoading, auth.userData]);

    useEffect(() => {
        if (listPrograms.length === 0 && currentPage > 1) {
            const previousPage = currentPage - 1;
            getPrograms(
                previousPage,
                auth.userData?.profile.preferred_username
            );
            setCurrentPage(previousPage);
        }
    }, [listPrograms, currentPage, auth.userData]);
    /////////////////useEffect////////////////
    const getPrograms = async (page, username) => {
        let res = await ProgramSerivce.fetchAllProgram(page, username);
        if (res && res.program_list) {
            setTotalPrograms(res.total);
            setListPrograms(res.program_list);
            setTotalPage(res.total_page);
        }
    };

    const [search, setSearch] = useState("");
    const [selectedStatus, setSelectedStatus] = useState("");

    // Thêm state mới để lưu trữ trang hiện tại của danh sách chương trình khi lọc theo trạng thái
    const [currentPageFiltered, setCurrentPageFiltered] = useState(1);

    const handleFilterByName = debounce(async (event) => {
        const { value } = event.target;
        setSearch(value); // Cập nhật giá trị của state search với giá trị mới từ trường nhập liệu
        setCurrentPageFiltered(1); // Reset trang về 1 khi thay đổi giá trị của trường nhập liệu
        try {
            // Gọi hàm lấy dữ liệu chương trình với trang, tên người dùng, và tên chương trình
            const res = await ProgramService.filterProgramByName(
                value,
                auth.userData?.profile.preferred_username,
                currentPage
            );
            if (res && res.program_list) {
                setTotalPrograms(res.total);
                setListPrograms(res.program_list);
                setTotalPage(res.total_page);
            }
        } catch (error) {
            console.error("Error filtering programs by name:", error);
        }
    }, 200);

    // Hàm mới để lấy dữ liệu chương trình với trang và trạng thái lọc
    const getProgramsFiltered = async (page, username, status) => {
        try {
            let res;
            if (status != "") {
                res = await ProgramService.filterProgramByStatus(
                    status,
                    username,
                    page
                );
            } else {
                res = await ProgramService.fetchAllProgram(page, username);
            }
            if (res && res.program_list) {
                setTotalPrograms(res.total);
                setListPrograms(res.program_list);
                setTotalPage(res.total_page);
            }
        } catch (error) {
            console.error("Error filtering programs by status:", error);
        }
    };
    // Hàm xử lý phân trang khi thay đổi trang hoặc trạng thái lọc
    const handlePageChangeFiltered = (selectedPage) => {
        setCurrentPage(selectedPage + 1); // Cập nhật currentPage với trang mới được chọn
        getProgramsFiltered(
            selectedPage + 1,
            auth.userData?.profile.preferred_username,
            selectedStatus
        ); // Gọi hàm để lấy dữ liệu mới
    };

    // Thêm sự kiện onChange cho dropdown lọc trạng thái để gọi hàm lọc chương trình mới
    const handleSelectedStatus = (event) => {
        setSelectedStatus(event.target.value);
        setCurrentPageFiltered(1); // Reset trang về 1 khi thay đổi trạng thái lọc
        getProgramsFiltered(
            1,
            auth.userData?.profile.preferred_username,
            event.target.value
        ); // Gọi hàm để lấy dữ liệu mới khi thay đổi trạng thái lọc
    };

    return (
        <>
            <nav className="p-3 bg-light">
                <ul style={{ display: "flex", whiteSpace: "nowrap" }}>
                    <li style={{ display: "inline-block" }}>
                        <div>
                            <input
                                className="form-control"
                                placeholder="Filter by Name-DeadLine"
                                onChange={debounce(handleFilterByName, 200)} // Sử dụng hàm handleFilterByName
                            />
                        </div>
                    </li>
                    <li
                        style={{ display: "inline-block", marginLeft: "10px" }}
                    ></li>
                    <li style={{ display: "inline-block" }}>
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
                    <li
                        style={{
                            display: "inline-block",
                            marginLeft: "auto",
                            textAlign: "center",
                        }}
                    >
                        <Button
                            style={{ width: "125px", color: "white" }}
                            type="button"
                            className="btn btn-info "
                            onClick={handleCreateProgramClick}
                        >
                            Create Program
                        </Button>
                    </li>
                    <li style={{ display: "inline-block" }}>
                        <Button
                            style={{ width: "125px", color: "white" }}
                            type="button"
                            className=" btn-danger  ms-2 "
                            onClick={deleteSelectedPrograms}
                            disabled={!isDeleteButtonEnabled}
                        >
                            Delete
                        </Button>
                    </li>
                </ul>

                {showCreateProgram && (
                    <CreateProgram onClose={handleCloseCreateProgram} />
                )}
                {listPrograms.length === 0 && currentPage === 1 ? (
                    <h3 style={{ textAlign: "center" }}>
                        There are no Program
                    </h3>
                ) : (
                    <>
                        <div className="table-wrapper">
                            <table className="table caption-top bg-white rounded table-striped">
                                <thead>
                                    <tr>
                                        <th scope="col">
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
                                                    onChange={handleCheckAll} // Attach the event handler here
                                                />
                                            </Form>
                                        </th>

                                        <th scope="col" href="/TaskPage">
                                            Program Name
                                        </th>
                                        <th scope="col">Create Date</th>
                                        <th scope="col">Deadline</th>
                                        <th scope="col">Status</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {listPrograms.map((item) => (
                                        <tr key={item.id}>
                                            <th>
                                                <Form
                                                    className="CheckOption"
                                                    style={{
                                                        display: "flex",
                                                        justifyContent:
                                                            "center",
                                                    }}
                                                >
                                                    <Form.Check
                                                        type="checkbox"
                                                        onChange={() =>
                                                            toggleProgramSelection(
                                                                item.id
                                                            )
                                                        }
                                                        checked={selectedPrograms.includes(
                                                            item.id
                                                        )}
                                                    />
                                                </Form>
                                            </th>
                                            <td>
                                                <Link
                                                    to={`/TaskPage/${item.id}/${item.name}/${item.end_time}`}
                                                    className="nav-link"
                                                >
                                                    {item.name}
                                                </Link>
                                            </td>
                                            <td>
                                                <Link
                                                    to={`/TaskPage/${item.id}/${item.name}/${item.end_time}`}
                                                    className="nav-link"
                                                >
                                                    {formatDate(
                                                        item.create_time
                                                    )}
                                                </Link>
                                            </td>
                                            <td>
                                                <Link
                                                    to={`/TaskPage/${item.id}/${item.name}/${item.end_time}`}
                                                    className="nav-link"
                                                >
                                                    {formatDate(item.end_time)}
                                                </Link>
                                            </td>
                                            <td
                                                style={{
                                                    color:
                                                        item.status ===
                                                        "MISS_DEADLINE"
                                                            ? "red"
                                                            : item.status ===
                                                              "IN_PROGRESS"
                                                            ? "orange"
                                                            : "green",
                                                }}
                                            >
                                                <Link
                                                    to={`/TaskPage/${item.id}/${item.name}/${item.end_time}`}
                                                    className="nav-link"
                                                >
                                                    {item.status}
                                                </Link>
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
                                    handlePageChangeFiltered(
                                        selectedPage.selected
                                    )
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
        </>
    );
};
export default TablePrograms;
