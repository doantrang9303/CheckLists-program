import React from 'react';
import './Navbar.css'

function Navbar() {
    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-custom1">
            <div className="container-fluid">
               
                <ul className="navbar-nav ms-auto mb-2 mb-lg-0">
                    <div onClick={Navbar}>
                  <img src="https://cdn.icon-icons.com/icons2/1147/PNG/512/1486486295-alert-bell-christmas-bell-church-bell-notification_81202.png" className="img-fluid my-3" width="50" height="50"/>
                </div>
                </ul>
            </div>
        </nav>
    )
}
export default Navbar