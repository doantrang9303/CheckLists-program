<<<<<<<< HEAD:Front-end/src/TokenUpdater.jsx
import  { useEffect, useState } from 'react';
import { useAuth } from 'oidc-react';

const TokenUpdater = () => {
  const auth = useAuth();
  const [storedToken, setStoredToken] = useState(localStorage.getItem('access_token'));

  useEffect(() => {
    const intervalId = setInterval(() => {
      const currentToken = auth.userData?.access_token;
      if (currentToken && currentToken !== storedToken) {
        localStorage.setItem('access_token', currentToken);
        setStoredToken(currentToken);
      }
    }, 10000); // Check every 10 seconds

    return () => clearInterval(intervalId); // Cleanup on unmount
  }, [auth.userData, storedToken]);

  // This component doesn't render anything, so return null
  return null; 
};

========
import React, { useEffect, useState } from 'react';
import { useAuth } from 'oidc-react';

const TokenUpdater = () => {
  const auth = useAuth();
  const [storedToken, setStoredToken] = useState(localStorage.getItem('access_token'));

  useEffect(() => {
    const interval = setInterval(() => {
      const currentToken = auth.userData?.access_token;
      if (currentToken && currentToken !== storedToken) {
        console.log('Token has changed, updating local storage.');
        localStorage.setItem('access_token', currentToken);
        setStoredToken(currentToken);
      }
    }, 10000); // Check every 10000ms (10 second)

    return () => clearInterval(interval); 
  }, [storedToken, auth.userData]);

  return null; 

};
>>>>>>>> origin/sang-fe:front_end/src/TokenUpdater.jsx
export default TokenUpdater;