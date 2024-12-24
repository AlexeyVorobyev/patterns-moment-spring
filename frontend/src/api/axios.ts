import axios from 'axios'

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8083/api', // Замените на реальный baseUrl
    headers: {
        'Content-Type': 'application/json',
    },
});

export default axiosInstance;
