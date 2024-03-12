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
export default TokenUpdater;