// src/hooks/useAuth.js
import { createContext, useContext, useState, useEffect } from 'react';
import Keycloak from 'keycloak-js';


const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [auth, setAuth] = useState({ keycloak: null, authenticated: false, username: "", initialized: false, access_token:"" });

  useEffect(() => {
    const keycloakConfig = {
      url: import.meta.env.VITE_KEYCLOAK_URL,
      realm: import.meta.env.VITE_KEYCLOAK_REALM,
      clientId: import.meta.env.VITE_KEYCLOAK_CLIENT,
    };
   const keycloak = new Keycloak(keycloakConfig);
    keycloak.init({ onLoad: 'login-required', checkLoginIframe: false }).then(authenticated => {
      console.log('1', keycloak);
      console.log('2', authenticated);
      console.log('3', keycloak.tokenParsed?.access_token)
      setAuth({
        keycloak,
        authenticated,
        initialized: true,
        username: keycloak.tokenParsed?.preferred_username || "",
        access_token : keycloak.tokenParsed?.access_token 
      });
    });
  }, []);

  const logout = () => {
    auth.keycloak.logout();
  };

  return (
    <AuthContext.Provider value={{ ...auth, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
