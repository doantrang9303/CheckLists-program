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

export default TokenUpdater;