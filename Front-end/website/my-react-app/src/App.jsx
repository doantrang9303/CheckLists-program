import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import Sidebar from './Sidebar';
import Navbar from './Navbar';
import Home from './Home';
import { AuthProvider } from 'oidc-react';
import TokenUpdater from './TokenUpdater';

const oidcConfig = {
    onSignIn: async (userData) => {
      console.log('user data', userData);
      localStorage.setItem('access_token', userData.access_token);
      // Navigate to your desired route after login
    },
    authority: 'http://localhost:8080/realms/react-test',
    clientId: 'myclient',
    clientSecret: 'EXbUdVCyKJ5NUitgZi5CBINt85dkoNlx',
    responseType: 'code',
    redirectUri: window.location.origin,
    // autoSignIn: true,
    automaticSilentRenew: true,
    scope: 'openid profile email',
  };

function App() {
    return (
        <AuthProvider {...oidcConfig}>
        <TokenUpdater />
          <div className='d-flex bg-light vh-100'>
            <div className='w-auto'>
              <Sidebar />
            </div>
            <div className='col'>
              <Navbar />
              <Home />
            </div>
          </div>
      </AuthProvider>
    );
  }

export default App;
