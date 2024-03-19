import axios from './customize-axios';


<<<<<<< Updated upstream
const fetchAllProgram = (page,username ) => { 
  return axios.get(`/programs/all?page=${page}`, {
=======
const fetchAllProgram = (page,username) => { 
  return axios.get(`/programs?page=${page}`, {
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
const ProgramService = {
  fetchAllProgram,
  createProgram,
=======


const filterProgramByStatus = (status, username) => {
  return axios.get(`/programs?status=${status}`, 
  {
    headers: {
      'user_name': username || ''
    }
  }); 
}
const deleteProgram = (programId) => {
  return axios.delete(`/programs/delete/${programId}`);
}
const ProgramService = {
  fetchAllProgram,
  createProgram,
  deleteProgram,
  filterProgramByStatus
  
>>>>>>> Stashed changes
}

export  default ProgramService;
