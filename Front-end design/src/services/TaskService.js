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

const TaskService = {
  fetchAllTask,
  createTask,
  deleteTask,
  editTask,
};

export default TaskService;