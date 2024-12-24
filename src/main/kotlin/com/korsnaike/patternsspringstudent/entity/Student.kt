package com.korsnaike.patternsspringstudent.entity

import com.korsnaike.patternsspringstudent.dto.UpdateStudentDto
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import java.util.*

import com.korsnaike.patternsspringstudent.utils.VALID_EMAIL_ADDRESS_REGEX_WITH_EMPTY_SPACES_ACCEPTANCE
import com.korsnaike.patternsspringstudent.validators.unique.UniqueValue

@Entity
@Table(name = "student")
data class Student(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "first_name", nullable = false)
    val firstName: String,

    @Column(name = "last_name", nullable = false)
    val lastName: String,

    @Column(name = "middle_name", nullable = false)
    val middleName: String,

    @Column(name = "email", unique = true, nullable = true)
    val email: String? = null,

    @Column(name = "telegram", unique = true, nullable = true)
    val telegram: String? = null,

    @Column(name = "phone", unique = true, nullable = true)
    val phone: String? = null,

    @Column(name = "git", unique = true, nullable = true)
    val git: String? = null,

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt: Date? = Date()
) {
    @PrePersist
    fun prePersist() {
        createdAt = Date() // Set the createdAt date only when the entity is persisted
    }

    fun updateFromDto(dto: UpdateStudentDto): Student {
        return this.copy(
            firstName = dto.firstName ?: this.firstName,
            lastName = dto.lastName ?: this.lastName,
            middleName = dto.middleName ?: this.middleName
        )
    }
}
