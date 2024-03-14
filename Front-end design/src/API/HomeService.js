import axios from "axios";
import {getErrorMessage} from "./errorMessage.js";

const addProgram =(programDTO) => {
    return axios.post("http://localhost:9292/programs/add",programDTO);
}
const HomeService = {
    addProgram
}
export default HomeService