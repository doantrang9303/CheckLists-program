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
import { debounce } from 'lodash';
import 'react-toastify/dist/ReactToastify.css';
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
        if (res && res.program_list) {
            setTotalPrograms(res.total);
            setListPrograms(res.program_list);
            setTotalPage(res.total_page);
        }
    }

    const [search, setSearch] = useState('');
    console.log(search)
    const [selectedStatus, setSelectedStatus] = useState('');

    // Thêm state mới để lưu trữ trang hiện tại của danh sách chương trình khi lọc theo trạng thái
    const [currentPageFiltered, setCurrentPageFiltered] = useState(1);

    const handleSearchChange = (event) => {
        setSearch(event.target.value); // Cập nhật giá trị tìm kiếm
        setCurrentPageFiltered(1); // Cập nhật trang về 1
    };

    // Hàm mới để lấy dữ liệu chương trình với trang và trạng thái lọc
    const getProgramsFiltered = async (page, username, status) => {
        try {
            let res;
            if (status != "") {
                res = await ProgramSerivce.filterProgramByStatus(status, username, page);
            }
            else {
                res = await ProgramSerivce.fetchAllProgram(page, username);
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
        setCurrentPageFiltered(selectedPage + 1); // Cập nhật trang hiện tại
        getProgramsFiltered(selectedPage + 1, auth.userData?.profile.preferred_username, selectedStatus); // Gọi hàm để lấy dữ liệu mới
    };
    // Thêm sự kiện onChange cho dropdown lọc trạng thái để gọi hàm lọc chương trình mới
    const handleSelectedStatus = (event) => {
        setSelectedStatus(event.target.value);
        setCurrentPageFiltered(1); // Reset trang về 1 khi thay đổi trạng thái lọc
        getProgramsFiltered(1, auth.userData?.profile.preferred_username, event.target.value); // Gọi hàm để lấy dữ liệu mới khi thay đổi trạng thái lọc
    };

    return (
        <>
            <nav className="p-3 bg-light">

                <ul style={{ display: 'flex', whiteSpace: 'nowrap' }}>
                    <li style={{ display: 'inline-block' }}>
                        <div>
                            <input
                                className='form-control'
                                placeholder='Filter by Name-DeadLine'
                                // onChange={debounce((event) => setSearch(event.target.value), 200)}
                                onChange={debounce(handleSearchChange, 200)} // Sử dụng hàm handleSearchChange
                            />
                        </div>
                    </li>
                    <li style={{ display: 'inline-block' , marginLeft: '10px' }}>

                    </li>
                    <li style={{ display: 'inline-block' }}>
                        <Form.Select aria-label="Default select example" className="status" onChange={handleSelectedStatus} >
                            <option value="">Filter by Status</option>
                            <option value="In_progress">IN_PROGRESS</option>
                            <option value="Completed">COMPLETED</option>
                        </Form.Select>
                    </li>
                    <li style={{ display: 'inline-block', marginLeft: 'auto' }}>

                    <Button style={{ width: '125px', color:'white' }}
                        type="button"
                        className="btn btn-info "
                        onClick={handleCreateProgramClick}
                    >
                        Create Program
                    </Button>

                    </li>
                    <li style={{ display: 'inline-block' }}>
                        <Button style={{ width: '125px', color:'white' }}
                            type="button"
                            className=" btn-danger  ms-2 "
                            onClick={deleteSelectedPrograms}
                        >
                            Delete
                        </Button>
                    </li>

                </ul>

                {showCreateProgram && <CreateProgram onClose={handleCloseCreateProgram} />}
                {listPrograms.length === 0 && currentPage === 1 ? (
                    <h3 style={{textAlign: "center"}}>There are no Program</h3>
                ) :
                    <>
                        <table className="table table-hover caption-top bg-white rounded">
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
                                    listPrograms.filter((item) => {
                                        return search.toLowerCase() === '' ? item : (item.name.toLowerCase().includes(search) ||
                                            search.toLowerCase() === '' ? item : (formatDate(item.end_time).includes(search)));
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
                                                <td><Link to={`/TaskPage/${item.id}/${item.name}/${item.end_time}`} className='nav-link'>{item.name}</Link></td>
                                                <td><Link to={`/TaskPage/${item.id}/${item.name}/${item.end_time}`} className='nav-link'>{formatDate(item.create_time)}</Link></td>
                                                <td><Link to={`/TaskPage/${item.id}/${item.name}/${item.end_time}`} className='nav-link'>{formatDate(item.end_time)}</Link></td>
                                                <td><Link to={`/TaskPage/${item.id}/${item.name}/${item.end_time}`} className='nav-link'>{item.status}</Link></td>
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
                            // onPageChange={handlePageClick}
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
