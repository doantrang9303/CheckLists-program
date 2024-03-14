// import axios from "axios";

import axios from './customize-axios';

const fetchAllProgram =(page) => { 
return axios.get(`/program?page=${page}}`);   

}

// const postCreateUser = (name, job) => { 
//     return axios.post("/api/users", {name, job})
// }

// const putUpdateUser = (name, job)  => { 
//     return axios.put("/api/users/2", {name, job})
// }

export {fetchAllProgram}; //postCreateUser, putUpdateUser};