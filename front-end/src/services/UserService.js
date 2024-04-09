
import axios from 'axios';



const addUser = async (userName, email) => {


  try {
    const response = await axios.post('/users/add', null, {
      headers: {
        userName: userName,
        email: email,
      },
    });
    return response.data;
  } catch (error) {
    throw error.response.data;
  }
};

const UserService = {
  addUser,
};

export default UserService;