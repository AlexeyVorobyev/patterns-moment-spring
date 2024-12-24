import { toast } from 'react-toastify';

// Тип для ошибок (часть структуры из вашего примера)
interface ApiError {
    errors?: Array<{
        field?: string;
        defaultMessage?: string;
    }>;
    message?: string;
    status?: number;
}

// Функция для обработки ошибок
export const handleApiError = (error: any) => {
    const apiError = error.response?.data as ApiError;

    if (apiError?.errors && apiError.errors.length > 0) {
        apiError.errors.forEach((err) => {
            const field = err.field ? `${err.field}: ` : '';
            const message = err.defaultMessage || 'An error occurred';
            toast.error(`${field}${message}`, { position: 'top-right' });
        });
    } else if (apiError?.message) {
        toast.error(apiError.message, { position: 'top-right' });
    } else {
        toast.error('An unknown error occurred', { position: 'top-right' });
    }
};
