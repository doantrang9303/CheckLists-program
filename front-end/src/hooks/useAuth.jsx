// // src/hooks/useAuth.js
// import { createContext, useContext, useState, useEffect } from "react";
// import Keycloak from "keycloak-js";
// import UserService from "../services/UserService";

// const AuthContext = createContext();

// export const AuthProvider = ({ children }) => {
//     const [auth, setAuth] = useState({
//         keycloak: null,
//         authenticated: false,
//         username: "",
//         initialized: false,
//         access_token: "",
//         email: "",
//     });

//     useEffect(() => {
//         const keycloakConfig = {
//             url: import.meta.env.VITE_KEYCLOAK_URL,
//             realm: import.meta.env.VITE_KEYCLOAK_REALM,
//             clientId: import.meta.env.VITE_KEYCLOAK_CLIENT,
//         };
//         const keycloak = new Keycloak(keycloakConfig);
//         keycloak
//             .init({ onLoad: "login-required", checkLoginIframe: false })
//             .then((authenticated) => {
//                 console.log("1", keycloak);
//                 console.log("2", authenticated);
//                 console.log("3", keycloak.tokenParsed?.access_token);
//                 setAuth({
//                     keycloak,
//                     authenticated,
//                     initialized: true,
//                     username: keycloak.tokenParsed?.preferred_username || "",
//                     access_token: keycloak.tokenParsed?.access_token,
//                     email: keycloak.tokenParsed?.email || "",
//                 });
//                 // Gọi hàm addUser khi người dùng được xác thực thành công
//                 if (authenticated) {
//                     try {
//                         UserService.addUser(
//                             keycloak.tokenParsed?.preferred_username || "",
//                             keycloak.tokenParsed?.email
//                         );
//                         console.log("User added successfully.");
//                     } catch (error) {
//                         console.error("Failed to add user:", error);
//                     }
//                 }
//             });
//     }, []);

//     const logout = () => {
//         auth.keycloak.logout();
//     };

//     return (
//         <AuthContext.Provider value={{ ...auth, logout }}>
//             {children}
//         </AuthContext.Provider>
//     );
// };

// export const useAuth = () => useContext(AuthContext);
import { createContext, useContext, useState, useEffect } from "react";
import Keycloak from "keycloak-js";
import UserService from "../services/UserService";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [auth, setAuth] = useState({
        keycloak: null,
        authenticated: false,
        username: "",
        initialized: false,
        access_token: "",
        email: "",
    });

    useEffect(() => {
        const keycloakConfig = {
            url: import.meta.env.VITE_KEYCLOAK_URL,
            realm: import.meta.env.VITE_KEYCLOAK_REALM,
            clientId: import.meta.env.VITE_KEYCLOAK_CLIENT,
        };
        const keycloak = new Keycloak(keycloakConfig);
        keycloak
            .init({ onLoad: "login-required", checkLoginIframe: false })
            .then((authenticated) => {
                setAuth({
                    keycloak,
                    authenticated,
                    initialized: true,
                    username: keycloak.tokenParsed?.preferred_username || "",
                    access_token: keycloak.tokenParsed?.access_token,
                    email: keycloak.tokenParsed?.email || "",
                });
                // Gọi hàm addUser khi người dùng được xác thực thành công
                if (authenticated) {
                    try {
                        UserService.addUser(
                            keycloak.tokenParsed?.preferred_username || "",
                            keycloak.tokenParsed?.email || ""
                        )
                            .then(() => {
                                console.log("User added successfully.");
                            })
                            .catch((error) => {
                                console.error("Failed to add user:", error);
                            });
                    } catch (error) {
                        console.error("Failed to add user:", error);
                    }
                }
            });
    }, []);

    const logout = () => {
        auth.keycloak && auth.keycloak.logout();
    };

    return (
        <AuthContext.Provider value={{ ...auth, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
