import React from 'react';
import './Navbar.css'
import { useAuth } from 'oidc-react';
import { Link, useParams } from 'react-router-dom';
function Navbar() {
    const auth = useAuth();
    const { id } = useParams();

    const handleSignOut = () => {
        localStorage.removeItem('access_token');
        const logoutUrl = `${process.env.REACT_APP_KEYCLOAK_URL}/realms/${process.env.REACT_APP_KEYCLOAK_REALM}/protocol/openid-connect/logout?post_logout_redirect_uri=${encodeURIComponent(window.location.origin)}&id_token_hint=${auth.userData.id_token}`;
        auth.signOut();
        window.location.href = logoutUrl;
    };
    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-custom1">
            <div className="container-fluid">
            {id && (<Link to="/" style={{ textDecoration: 'none', width: '150px' }}>
                    <img src="https://cdn.iconscout.com/icon/free/png-256/free-back-159-386746.png?f=webp" width="40" height="40" />
                </Link>
            )}
                <ul className="navbar-nav ms-auto mb-2 mb-lg-0">
                    <div className="d-flex align-items-center">
                        <span className="brand-name fs-6"> {`${auth.userData?.profile.given_name} ${auth.userData?.profile.family_name}`}</span>
                        <button type="button" className="m-2 btn btn-custom btn-outline-dark btn-sm" onClick={handleSignOut}>Log out</button>
                    </div>
                </ul>
            </div>
        </nav>
    )
}
export default Navbar  