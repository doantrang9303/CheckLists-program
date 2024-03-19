import axios from './customize-axios';


const fetchAllProgram = (page,username ) => { 
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
const ProgramService = {
  fetchAllProgram,
  createProgram,
  deleteProgram,
}

export  default ProgramService;
