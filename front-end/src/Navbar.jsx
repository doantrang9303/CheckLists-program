import React from 'react';
import './Navbar.css';
import { useAuth } from 'oidc-react';
import { Link } from 'react-router-dom';
function Navbar() {
    const auth = useAuth();
   

    const handleSignOut = () => {
        localStorage.removeItem('access_token');
        const logoutUrl = `${process.env.REACT_APP_KEYCLOAK_URL}/realms/${process.env.REACT_APP_KEYCLOAK_REALM}/protocol/openid-connect/logout?post_logout_redirect_uri=${encodeURIComponent(window.location.origin)}&id_token_hint=${auth.userData.id_token}`;
        auth.signOut();
        window.location.href = logoutUrl;
    };
    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-custom1">
            <div className="container-fluid">
           <Link to="/" style={{ textDecoration: 'none', width: '150px' }}>
                    <img src="https://fptplayboxdongnai.com/wp-content/uploads/2020/04/icon-home-cam-400x400.png" alt='home' width="50" height="50" />
                </Link>
        
                <ul className="navbar-nav ms-auto mb-2 mb-lg-0">
                    <div className="d-flex align-items-center">
                        <span className="brand-name fs-6"> {`${auth.userData?.profile.given_name} ${auth.userData?.profile.family_name}`}</span>
                        <button type="button" className="m-2 btn btn-secondary btn-sm" onClick={handleSignOut}>Log out</button>
                    </div>
                </ul>
            </div>
        </nav>
    )
}
export default Navbar  