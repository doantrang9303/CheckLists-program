import axios from "axios";

const instance = axios.create({ 
    baseURL: process.env.REACT_APP_API_SSO_VERIFY_ACCESS_TOKEN
});

//Add a response interceptor 
instance.interceptors.response.use(function(response) { 
    //Any status code that lie within the range of 2xx cause this function to trigger 
    //Do something with response data 
    return response.data; 
  }, function(error) {
    // Any status codes that falls outside the range ò 2xx cause thí function to trigger
    // Do something with response error
    return Promise.reject(error); 
  });

export default instance;

// const callApi = async () => {
//   const accessToken = localStorage.getItem('access_token');
//   console.log(accessToken)
//   try {
//       const response = await axios.post(process.env.REACT_APP_KEYCLOAK_VERIFY , {}, {
//           headers: {
//               'Authorization': `Bearer ${accessToken}` // Include the access token in the Authorization header
//           }
//       })

//       alert(response.data);
//       // Handle your response data
//   } catch (error) {
