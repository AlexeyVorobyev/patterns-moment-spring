package com.korsnaike.patternsspringstudent.service

import com.korsnaike.patternsspringstudent.dto.GetAllStudentsQueryParamsDto
import com.korsnaike.patternsspringstudent.entity.Student
import com.korsnaike.patternsspringstudent.repository.StudentRepository
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*

@Service
class StudentService(@Autowired private val studentRepository: StudentRepository) {

    fun findById(id: Long): Optional<Student> = studentRepository.findById(id)

    fun save(student: Student): Student = studentRepository.save(student)

    fun update(@Valid student: Student): Student {
        if (!studentRepository.existsById(student.id)) {
            val id = student.id
            throw NoSuchElementException("Student with id $id not found")
        }
        return studentRepository.save(student)
    }

    fun deleteById(id: Long) {
        if (!studentRepository.existsById(id)) {
            throw NoSuchElementException("Student with id $id not found")
        }
        studentRepository.deleteById(id)
    }

    fun findAll(request: GetAllStudentsQueryParamsDto): Page<Student> {
        // Создаем Pageable на основе параметров запроса
        val pageable = PageRequest.of(
            request.page,
            request.size,
            if (request.direction.uppercase() == "ASC") Sort.by(request.sortBy).ascending()
            else Sort.by(request.sortBy).descending()
        )

        // Создаем Specification для фильтрации данных
        val spec = Specification.where<Student>(null)
            .and(request.firstName?.let { Specification { root, _, cb -> cb.equal(root.get<String>("firstName"), it) } })
            .and(request.lastName?.let { Specification { root, _, cb -> cb.equal(root.get<String>("lastName"), it) } })
            .and(request.middleName?.let { Specification { root, _, cb -> cb.equal(root.get<String>("middleName"), it) } })
            .and(request.email?.let { Specification { root, _, cb -> cb.equal(root.get<String>("email"), it) } })
            .and(request.telegram?.let { Specification { root, _, cb -> cb.equal(root.get<String>("telegram"), it) } })
            .and(request.phone?.let { Specification { root, _, cb -> cb.equal(root.get<String>("phone"), it) } })
            .and(request.git?.let { Specification { root, _, cb -> cb.equal(root.get<String>("git"), it) } })

        // Выполняем запрос с использованием Specification и Pageable
        return studentRepository.findAll(spec, pageable)
    }
}