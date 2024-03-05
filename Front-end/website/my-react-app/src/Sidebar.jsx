import React from 'react';
import './Sidebar.css';
import { useAuth } from 'oidc-react';

function Sidebar() {
    const auth = useAuth();

    const handleSignOut = () => {
        localStorage.removeItem('access_token');
        // Construct the Keycloak logout URL
        const logoutUrl = `${import.meta.env.VITE_KEYCLOAK_URL}/realms/${import.meta.env.VITE_KEYCLOAK_REALM}/protocol/openid-connect/logout?post_logout_redirect_uri=${encodeURIComponent(window.location.origin)}&id_token_hint=${auth.userData.id_token}`;
        auth.signOut();
        
        window.location.href = logoutUrl;
      };
    return (
        <div className=' bg-custom sidebar vh-100'>
            <div className='Logo-web'>
                <img src="https://cdn.iconscout.com/icon/free/png-256/free-check-list-4002874-3305560.png" className="img-fluid my-3" width="60" height="60" />
                <span className="brand-name fs-4">WorkFlowMaster</span>
            </div>
            <div className='User'>
                <div>
                    <img src="https://cdn.icon-icons.com/icons2/1879/PNG/512/iconfinder-2-avatar-2754578_120514.png" className="m-custom2  img-fluid my-2" width="100" height="100"/>
                </div>
                <span className=" m-custom1 brand-name fs-4">{auth.userData?.profile.given_name}</span>
            </div>
            <button type="button" className="m-2 m-custom btn btn-custom btn-outline-dark btn-sm" onClick={handleSignOut}>Log out</button>
            <hr className='text-dark' />
            <button type="button" className="text-center m-3 btn btn-custom1 btn-outline-dark btn-lg fs-6" >List program of you</button>
            <div></div>
            <button type="button" className="text-center m-3 btn btn-custom2 btn-outline-dark btn-lg fs-6" >List program not approval</button>
        </div>
    );
}

export default Sidebar;