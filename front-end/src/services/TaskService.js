import axios from './customize-axios';

//moi sua
const fetchAllTask = (page, id) => {
  return axios.get(`/tasks/${id}?page=${page}`);
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
const filterTaskByName = (name, id, page) => {
  return axios.get(`/tasks/${id}?task_name=${name}&page=${page}`);
}
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
  filterTaskByName,
  importFile
};

export default TaskService;