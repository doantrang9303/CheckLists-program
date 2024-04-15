import axios from "axios";

const addUser = async (userName, email) => {
    try {
        const response = await axios.post(
            `${process.env.REACT_APP_API_SSO_VERIFY_ACCESS_TOKEN}/users/add`,
            null,
            {
                headers: {
                    userName: userName,
                    email: email,
                },
            }
        );
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
};

const UserService = {
    addUser,
};

export default UserService;
