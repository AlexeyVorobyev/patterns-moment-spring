import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { deleteStudent, getAllStudents, createStudent, updateStudent, Student, StudentFilters } from '../api/students';
import {
    Button,
    Pagination,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    TextField,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions, TableSortLabel
} from '@mui/material'
import {handleApiError} from '../errorHandler'

const StudentTable: React.FC = () => {
    const queryClient = useQueryClient();
    const [filters, setFilters] = useState<StudentFilters>({
        firstName: '',
        lastName: '',
        middleName: '',
        email: '',
        telegram: '',
        phone: '',
        git: '',
        page: 0,
        size: 10,
        sortBy: 'id',
        direction: 'ASC',
    });

    const [modalOpen, setModalOpen] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const [currentStudent, setCurrentStudent] = useState<Partial<Student>>({});

    const cleanFilters = (filters: StudentFilters) => {
        return Object.fromEntries(
            Object.entries(filters).filter(([_, value]) => value !== '' && value !== undefined)
        ) as StudentFilters;
    };

    const { data, isLoading } = useQuery(['students', filters], () => getAllStudents(cleanFilters(filters)), {
        keepPreviousData: true,
    });

    const createMutation = useMutation(createStudent, {
        onSuccess: () => {
            queryClient.invalidateQueries('students');
            setModalOpen(false);
        },
        onError: (error) => {
            handleApiError(error);
        },
    });

    const updateMutation = useMutation(({ id, student }: { id: number; student: Partial<Student> }) => updateStudent(id, student), {
        onSuccess: () => {
            queryClient.invalidateQueries('students');
            setModalOpen(false);
        },
        onError: (error) => {
            handleApiError(error);
        },
    });

    const deleteMutation = useMutation(deleteStudent, {
        onSuccess: () => queryClient.invalidateQueries('students'),
        onError: (error) => {
            handleApiError(error);
        },
    });

    const handleFilterChange = (field: keyof StudentFilters, value: string | number) => {
        setFilters((prev) => ({
            ...prev,
            [field]: value,
        }));
    };

    const handleOpenModal = (student?: Student) => {
        setCurrentStudent(student || {});
        setIsEditing(!!student);
        setModalOpen(true);
    };

    const handleCloseModal = () => {
        setModalOpen(false);
        setCurrentStudent({});
    };

    const handleSave = () => {
        if (isEditing && currentStudent.id) {
            updateMutation.mutate({ id: currentStudent.id, student: currentStudent });
        } else {
            createMutation.mutate(currentStudent as Omit<Student, 'id'>);
        }
    };

    const handleSort = (column: keyof Student) => {
        setFilters((prev) => ({
            ...prev,
            sortBy: column,
            direction: prev.sortBy === column && prev.direction === 'ASC' ? 'DESC' : 'ASC',
        }));
    };

    const headers = [
        ["First name", "firstName"],
        ["Last name", "lastName"],
        ["Middle name", "middleName"],
        ["Email", "email"],
        ["Telegram", "telegram"],
        ["Phone", "phone"],
        ["Git", "git"],
    ];


    if (isLoading) return <div>Loading...</div>;

    return (
        <div>
            <div style={{ marginBottom: '16px' }}>
                <TextField
                    label="First Name"
                    variant="outlined"
                    size="small"
                    value={filters.firstName}
                    onChange={(e) => handleFilterChange('firstName', e.target.value)}
                    style={{ marginRight: '8px' }}
                />
                <TextField
                    label="Last Name"
                    variant="outlined"
                    size="small"
                    value={filters.lastName}
                    onChange={(e) => handleFilterChange('lastName', e.target.value)}
                    style={{ marginRight: '8px' }}
                />
                <TextField
                    label="Middle Name"
                    variant="outlined"
                    size="small"
                    value={filters.middleName}
                    onChange={(e) => handleFilterChange('middleName', e.target.value)}
                    style={{ marginRight: '8px' }}
                />
                <TextField
                    label="Email"
                    variant="outlined"
                    size="small"
                    value={filters.email}
                    onChange={(e) => handleFilterChange('email', e.target.value)}
                    style={{ marginRight: '8px' }}
                />
                <TextField
                    label="Telegram"
                    variant="outlined"
                    size="small"
                    value={filters.telegram}
                    onChange={(e) => handleFilterChange('telegram', e.target.value)}
                    style={{ marginRight: '8px' }}
                />
                <TextField
                    label="Phone"
                    variant="outlined"
                    size="small"
                    value={filters.phone}
                    onChange={(e) => handleFilterChange('phone', e.target.value)}
                    style={{ marginRight: '8px' }}
                />
                <TextField
                    label="Git"
                    variant="outlined"
                    size="small"
                    value={filters.git}
                    onChange={(e) => handleFilterChange('git', e.target.value)}
                    style={{ marginRight: '8px' }}
                />
            </div>
            <Button variant="contained" color="primary" onClick={() => handleOpenModal()} style={{ marginBottom: '16px' }}>
                Add Student
            </Button>
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            {headers.map(([title, code]) => (
                                <TableCell>
                                    <TableSortLabel
                                        active={filters.sortBy === code}
                                        direction={filters.direction.toLowerCase() as 'asc' | 'desc'}
                                        onClick={() => handleSort(code as keyof Student)}
                                    >
                                        {title}
                                    </TableSortLabel>
                                </TableCell>
                            ))}
                            <TableCell>Actions</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {data?.content.map((student: Student) => (
                            <TableRow key={student.id}>
                                <TableCell>{student.firstName}</TableCell>
                                <TableCell>{student.lastName}</TableCell>
                                <TableCell>{student.middleName}</TableCell>
                                <TableCell>{student.email}</TableCell>
                                <TableCell>{student.telegram}</TableCell>
                                <TableCell>{student.phone}</TableCell>
                                <TableCell>{student.git}</TableCell>
                                <TableCell>
                                    <Button onClick={() => deleteMutation.mutate(student.id)}>Delete</Button>
                                    <Button onClick={() => handleOpenModal(student)}>Update</Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
                <Pagination
                    count={data?.totalPages}
                    page={filters.page + 1}
                    onChange={(_, page) => handleFilterChange('page', page - 1)}
                />
            </TableContainer>

            <Dialog open={modalOpen} onClose={handleCloseModal}>
                <DialogTitle>{isEditing ? 'Update Student' : 'Create Student'}</DialogTitle>
                <DialogContent>
                    <TextField
                        label="First Name"
                        fullWidth
                        margin="dense"
                        value={currentStudent.firstName || ''}
                        onChange={(e) => setCurrentStudent({ ...currentStudent, firstName: e.target.value })}
                    />
                    <TextField
                        label="Last Name"
                        fullWidth
                        margin="dense"
                        value={currentStudent.lastName || ''}
                        onChange={(e) => setCurrentStudent({ ...currentStudent, lastName: e.target.value })}
                    />
                    <TextField
                        label="Middle Name"
                        fullWidth
                        margin="dense"
                        value={currentStudent.middleName || ''}
                        onChange={(e) => setCurrentStudent({ ...currentStudent, middleName: e.target.value })}
                    />
                    {!isEditing && (
                        <>
                            <TextField
                                label="Email"
                                fullWidth
                                margin="dense"
                                value={currentStudent.email || ''}
                                onChange={(e) => setCurrentStudent({ ...currentStudent, email: e.target.value })}
                            />
                            <TextField
                                label="Telegram"
                                fullWidth
                                margin="dense"
                                value={currentStudent.telegram || ''}
                                onChange={(e) => setCurrentStudent({ ...currentStudent, telegram: e.target.value })}
                            />
                            <TextField
                                label="Phone"
                                fullWidth
                                margin="dense"
                                value={currentStudent.phone || ''}
                                onChange={(e) => setCurrentStudent({ ...currentStudent, phone: e.target.value })}
                            />
                            <TextField
                                label="Git"
                                fullWidth
                                margin="dense"
                                value={currentStudent.git || ''}
                                onChange={(e) => setCurrentStudent({ ...currentStudent, git: e.target.value })}
                            />
                        </>
                    )}
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseModal}>Cancel</Button>
                    <Button onClick={handleSave} color="primary">
                        Save
                    </Button>
                </DialogActions>
            </Dialog>
        </div>
    );
};

export default StudentTable;
