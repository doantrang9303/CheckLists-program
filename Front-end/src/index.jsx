<<<<<<<< HEAD:Front-end/src/index.jsx
// import React from 'react'
// import ReactDOM from 'react-dom/client'
// import App from './App.jsx'

// ReactDOM.createRoot(document.getElementById('root')).render(
//   // <React.StrictMode>
// <AuthProvider>
//     <App />
//</AuthProvider>
//   // </React.StrictMode>,
// )

// src/index.js or App.js
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.jsx';
import { BrowserRouter, Route, Routes, Navigate } from "react-router-dom";
import TaskPage from './Task/TaskPage.jsx';
import TablePrograms from './TablePrograms.jsx';
import { useAuth } from 'oidc-react';

function ProtectedRoute({ element, ...rest }) {
  const auth = useAuth();

  if (!auth.isAuthenticated()) {
    return <Navigate to/>;
  }

  return <Route {...rest} element={element} />;
}

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path={"/"} element={<App />} >
        <Route path={"TaskPage/:id/:name/:endate"} element={<TaskPage />} />
        <Route index element={<TablePrograms />} />
        </Route>
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
);
========
// import React from 'react'
// import ReactDOM from 'react-dom/client'
// import App from './App.jsx'

// ReactDOM.createRoot(document.getElementById('root')).render(
//   // <React.StrictMode>
// <AuthProvider>
//     <App />
//</AuthProvider>
//   // </React.StrictMode>,
// )

// src/index.js or App.js
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.jsx';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import TaskPage from './Task/TaskPage.jsx';
import TablePrograms from './TablePrograms.jsx';

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path={"/"} element={<App />} >
        <Route path={"TaskPage/:id/:name/:endate"} element={<TaskPage />} />
        <Route index element={<TablePrograms />} />
        </Route>
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
);
>>>>>>>> origin/sang-fe:front_end/src/index.jsx
