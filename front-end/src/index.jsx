import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App.jsx";
import { BrowserRouter, Route, Routes, Navigate } from "react-router-dom";
import TaskPage from "./Task/TaskPage.jsx";
import TablePrograms from "./TablePrograms.jsx";

ReactDOM.createRoot(document.getElementById("root")).render(
    <React.StrictMode>
        <BrowserRouter>
            <Routes>
                <Route path={"/"} element={<App />}>
                    <Route
                        path={"TaskPage/:id/:name/:endate"}
                        element={<TaskPage />}
                    />
                    <Route index element={<TablePrograms />} />
                </Route>
                <Route path="*" element={<Navigate to="/" />} />
            </Routes>
        </BrowserRouter>
    </React.StrictMode>
);
