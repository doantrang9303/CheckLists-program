import axios from './customize-axios';


const fetchAllProgram = (page,username ) => { 
  return axios.get(`/programs/all?page=${page}`, {
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
const ProgramService = {
  fetchAllProgram,
  createProgram,
}

export  default ProgramService;
