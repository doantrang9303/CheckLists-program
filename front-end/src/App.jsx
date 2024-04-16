import React from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import Navbar from "./Navbar";
import { AuthProvider } from "oidc-react";
import { useNavigate, Outlet } from "react-router-dom";
import UserService from "./services/UserService";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
const getOidcConfig = (navigate) => ({
    onSignIn: async (userData) => {
        console.log("user data", userData);
        localStorage.setItem("access_token", userData.access_token);
        const profileData = userData.profile || {};
        const username = profileData.preferred_username || "";
        const email = profileData.email || "";

        try {
            // Gọi hàm addUser từ UserService
            await UserService.addUser(username, email);
            console.log("User added successfully.");
        } catch (error) {
            console.error("Failed to add user:", error);
        }
        // Navigate to your desired route after login
        navigate("/");
    },
    authority: `${process.env.REACT_APP_KEYCLOAK_URL}/realms/${process.env.REACT_APP_KEYCLOAK_REALM}`,
    clientId: process.env.REACT_APP_KEYCLOAK_CLIENT,
    clientSecret: process.env.REACT_APP_KEYCLOAK_SECRET_CLIENT,
    responseType: "code",
    redirectUri: window.location.origin,
    // autoSignIn: true,
    automaticSilentRenew: true,
    scope: "openid profile email",
});

function App() {
    const navigate = useNavigate();
    const oidcConfig = getOidcConfig(navigate);

    return (
        <AuthProvider {...oidcConfig}>
            <div className="d-flex bg-light vh-100">
                <div className="w-auto"></div>
                <div className="col">
                    <Navbar />
                    <Outlet />
                    <ToastContainer />
                </div>
            </div>
        </AuthProvider>
    );
}

export default App;
