export const getErrorMessage = (error) => {
    return (error.response && error.response.data && error.response.data) || error.message || error.toString();
}
