package com.korsnaike.patternsspringstudent.controller

import com.korsnaike.patternsspringstudent.dto.GetAllStudentsQueryParamsDto
import com.korsnaike.patternsspringstudent.entity.Student
import com.korsnaike.patternsspringstudent.service.StudentService
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Valid
import jakarta.validation.ValidationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.transaction.TransactionException
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/students")
@Validated
class StudentController(@Autowired private val studentService: StudentService) {

    @GetMapping("/{id}")
    fun getStudentById(@PathVariable id: Long): Student =
        studentService.findById(id).orElseThrow { NoSuchElementException("Student with id $id not found") }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createStudent(@RequestBody @Valid student: Student): Student = studentService.save(student)

    @PutMapping("/{id}")
    fun updateStudent(@PathVariable id: Long, @RequestBody student: Student): Student {
        try {
            return studentService.update(student.copy(id = id))
        } catch (ex: TransactionException) {
            if (ex.rootCause != null && ex.rootCause is ValidationException) {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, (ex.rootCause as ValidationException).localizedMessage)
            }
            throw ex
        } catch (ex: ValidationException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.localizedMessage)
        }
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteStudent(@PathVariable id: Long) = studentService.deleteById(id)

    @GetMapping
    fun getAllStudents(
        @ModelAttribute @Valid request: GetAllStudentsQueryParamsDto
    ): Page<Student> {
        return studentService.findAll(request)
    }
}