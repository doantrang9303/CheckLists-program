import axios from './customize-axios';


const fetchAllProgram = (page, username) => {
  return axios.get(`/programs?page=${page}`, {
    headers: {
      // Include the username in the request headers if available
      'user_name': username || ''
    }
  });
}
const createProgram = (programData, username) => {
  return axios.post(`/programs/add`, programData, {
    headers: {
      'user_name': username || ''
    }
  });
}
const deleteProgram = (programId) => {
  return axios.delete(`/programs/delete/${programId}`);
}

const filterProgramByStatus = (status, username, page) => {
  return axios.get(`/programs?status=${status}&page=${page}`,
    {
      headers: {
        'user_name': username || ''
      }
    });
}
const filterProgramByName = (program_name, username, page) => {
  return axios.get(`/programs?program_name=${program_name}&page=${page}`,
    {
      headers: {
        'user_name': username || ''
      }
    });
}
const ProgramService = {
  fetchAllProgram,
  createProgram,
  deleteProgram,
  filterProgramByStatus,
  filterProgramByName
}

export default ProgramService;