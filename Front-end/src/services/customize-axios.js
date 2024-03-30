import axios from "axios";

const instance = axios.create({ 
    baseURL: 'http://localhost:9292'
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

//      if (error.response) {
//       // The request was made and the server responded with a status code
//       // that falls out of the range of 2xx
//       console.log(error.response.data);
//       console.log(error.response.status);
//       console.log(error.response.headers);
//   } else if (error.request) {
//       // The request was made but no response was received
//       // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
//       // http.ClientRequest in node.js
//       console.log(error.request);
//   } else {
//       // Something happened in setting up the request that triggered an Error
//       console.log('Error', error.message);
//   }
//   console.log(error.config);
//   }
// };