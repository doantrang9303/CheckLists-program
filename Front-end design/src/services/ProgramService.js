import axios from './customize-axios';


const fetchAllProgram = (page,username ) => { 
  return axios.get(`/programs/all?page=${page}`, {
    headers: {
      // Include the username in the request headers if available
      'user_name': username || ''
    }
  });   
}

export { fetchAllProgram };
