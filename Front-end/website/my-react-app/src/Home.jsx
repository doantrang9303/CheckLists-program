import React from 'react';
import './Home.css';
import axios from 'axios';

const callApi = async () => {
  const accessToken = localStorage.getItem('access_token');
  console.log(accessToken)
  try {
    const response = await axios.post('http://localhost:9292/verify-token', {}, {
        headers: {
          'Authorization': `Bearer ${accessToken}` // Include the access token in the Authorization header
        }
      })
    
    alert(response.data);
    // Handle your response data
  } catch (error) {
    console.error('Error making API call:', error);
  }
};

function Home() {
    return (
        <nav className="p-3 bg-light">
            <table className="table caption-top bg-white rounded">
                <thead>
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">Problem</th>
                        <th scope="col">Create Date</th>
                        <th scope="col">Owner</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <th scope="row">1</th>
                        <td>Hire Student</td>
                        <td>25/4/2024</td>
                        <td>Sang</td>
                    </tr>
                    <tr>
                        <th scope="row">2</th>
                        <td>Camping</td>
                        <td>22/4/2024</td>
                        <td>Sang</td>
                    </tr>
                    <tr>
                        <th scope="row">3</th>
                        <td >Party</td>
                        <td>21/4/2024</td>
                        <td>Sang</td>
                    </tr>
                    <tr>
                        <th scope="row">3</th>
                        <td >Party</td>
                        <td>21/4/2024</td>
                        <td>Sang</td>
                    </tr>
                    <tr>
                        <th scope="row">3</th>
                        <td >Party</td>
                        <td>21/4/2024</td>
                        <td>Sang</td>
                    </tr>
                    <tr>
                        <th scope="row">3</th>
                        <td >Party</td>
                        <td>21/4/2024</td>
                        <td>Sang</td>
                    </tr>
                    <tr>
                        <th scope="row">3</th>
                        <td >Party</td>
                        <td>21/4/2024</td>
                        <td>Sang</td>
                    </tr>
                    <tr>
                        <th scope="row">3</th>
                        <td >Party</td>
                        <td>21/4/2024</td>
                        <td>Sang</td>
                    </tr>
                    <tr>
                        <th scope="row">3</th>
                        <td >Party</td>
                        <td>21/4/2024</td>
                        <td>Sang</td>
                    </tr>
                    <tr>
                        <th scope="row">3</th>
                        <td >Party</td>
                        <td>21/4/2024</td>
                        <td>Sang</td>
                    </tr>
                    <tr>
                        <th scope="row">3</th>
                        <td >Party</td>
                        <td>21/4/2024</td>
                        <td>Sang</td>
                    </tr>
                    <tr>
                        <th scope="row">3</th>
                        <td >Party</td>
                        <td>21/4/2024</td>
                        <td>Sang</td>
                    </tr>

                </tbody>
            </table>
            <ul className="pagination justify-content-center">
                <li className="page-item"><a href="#" className="page-link">Previous</a></li>
                <li className="page-item"><a href="#" className="page-link">1</a></li>
                <li className="page-item"><a href="#" className="page-link">2</a></li>
                <li className="page-item"><a href="#" className="page-link">3</a></li>
                <li className="page-item"><a href="#" className="page-link">4</a></li>
                <li className="page-item"><a href="#" className="page-link">5</a></li>
                <li className="page-item"><a href="#" className="page-link">Next</a></li>
            </ul>
            <ul className="pagination justify-content-center">
            <button type="button" className="text-center btn btn-custom1 btn-outline-dark btn-lg fs-6" onClick={callApi}>Create new Program</button>
            </ul>
        </nav>
    )
}
export default Home