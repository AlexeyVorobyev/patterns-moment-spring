package com.korsnaike.patternsspringstudent.service

import com.korsnaike.patternsspringstudent.entity.Student
import com.korsnaike.patternsspringstudent.repository.StudentRepository
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*
import kotlin.NoSuchElementException

@Service
class StudentService(@Autowired private val studentRepository: StudentRepository) {

    fun findAll(): List<Student> = studentRepository.findAll()

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

    fun findWithFilters(
        firstName: String?,
        lastName: String?,
        middleName: String?,
        email: String?,
        telegram: String?,
        phone: String?,
        git: String?,
        pageable: Pageable
    ): Page<Student> {
        val spec = Specification.where<Student>(null)
            .and(firstName?.let { Specification<Student> { root, _, cb -> cb.equal(root.get<String>("firstName"), it) } })
            .and(lastName?.let { Specification<Student> { root, _, cb -> cb.equal(root.get<String>("lastName"), it) } })
            .and(middleName?.let { Specification<Student> { root, _, cb -> cb.equal(root.get<String>("middleName"), it) } })
            .and(email?.let { Specification<Student> { root, _, cb -> cb.equal(root.get<String>("email"), it) } })
            .and(telegram?.let { Specification<Student> { root, _, cb -> cb.equal(root.get<String>("telegram"), it) } })
            .and(phone?.let { Specification<Student> { root, _, cb -> cb.equal(root.get<String>("phone"), it) } })
            .and(git?.let { Specification<Student> { root, _, cb -> cb.equal(root.get<String>("git"), it) } })

        return studentRepository.findAll(spec, pageable)
    }
}