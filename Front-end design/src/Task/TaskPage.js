// TaskPage.js
import React from 'react';

const TaskPage = ({ programId }) => {
    // Lấy danh sách các công việc dựa trên programId và hiển thị chúng
    return (
        <div>
            <h1>Tasks of Program {programId}</h1>
            {/* Hiển thị danh sách các công việc */}
        </div>
    );
}

export default TaskPage;