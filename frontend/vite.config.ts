import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [react()],
    server: {
        port: 3000, // Укажите порт для dev-сервера
    },
    resolve: {
        alias: {
            '@': '/src', // Упрощенный импорт для src
        },
    },
})