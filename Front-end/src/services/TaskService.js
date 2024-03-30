import axios from './customize-axios';

const fetchAllTask = (page, id) => {
  return axios.get(`/tasks/${id}`, page);
}

const createTask = (taskData, programId) => {
  return axios.post(`/tasks/add`, taskData, {
    headers: {
      'program_id': programId || ''
    }
  });
}
const deleteTask = (taskId) => {
  return axios.delete(`/tasks/delete/${taskId}`);
}

const editTask = (taskId, updatedTaskData) => {
  return axios.put(`/tasks/update/${taskId}`, updatedTaskData);
};
const filterTaskByStatus = (status, id, page) => {
  return axios.get(`/tasks/${id}?status=${status}&page=${page}`);
}

// const importFile = (program_id) => { 
//   return axios.post(`/tasks/importTasksFromExcel?program_id=${program_id}`)
// }
const importFile = async (file, program_id) => {
  try {
    const formData = new FormData();
    formData.append('file', file);

    const response = await axios.post(`/tasks/importTasksFromExcel?program_id=${program_id}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });

    return response;
  } catch (error) {
    throw error;
  }
};

const TaskService = {
  fetchAllTask,
  createTask,
  deleteTask,
  editTask,
  filterTaskByStatus,
  importFile
};

export default TaskService;