import axiosInstance from './axios'

export interface Student {
    id: number;
    firstName: string;
    lastName: string;
    middleName?: string;
    email?: string;
    telegram?: string;
    phone?: string;
    git?: string;
}

export interface StudentFilters {
    firstName?: string;
    lastName?: string;
    middleName?: string;
    email?: string;
    telegram?: string;
    phone?: string;
    git?: string;
    page: number;
    size?: number;
    sortBy: string;
    direction: 'ASC' | 'DESC';
}

// api/students.ts
export const getAllStudents = async (filters?: StudentFilters) => {
    const response = await axiosInstance.get('/students', { params: filters });
    return response.data;
};
// Получение студента по ID
export const getStudentById = async (id: number) => {
    const response = await axiosInstance.get(`/students/${id}`);
    return response.data;
};

// Создание студента
export const createStudent = async (student: Omit<Student, 'id'>) => {
    const response = await axiosInstance.post('/students', student);
    return response.data;
};

// Обновление студента
export const updateStudent = async (id: number, student: Partial<Student>) => {
    // Отфильтровываем только нужные поля
    const payload = {
        firstName: student.firstName,
        lastName: student.lastName,
        middleName: student.middleName,
    };

    const response = await axiosInstance.patch(`/students/${id}`, payload);
    return response.data;
};

// Удаление студента
export const deleteStudent = async (id: number) => {
    await axiosInstance.delete(`/students/${id}`);
};
