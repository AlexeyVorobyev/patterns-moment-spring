package com.korsnaike.patternsspringstudent.repository

import com.korsnaike.patternsspringstudent.entity.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface StudentRepository : JpaRepository<Student, Long>, JpaSpecificationExecutor<Student>