package com.korsnaike.patternsspringstudent.entity

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
    @field:Email(message = "{validation.field.email.invalid-format}")
    @field:Pattern(
        regexp = VALID_EMAIL_ADDRESS_REGEX_WITH_EMPTY_SPACES_ACCEPTANCE,
        message = "{validation.field.email.invalid-format.cyrillic.not.allowed}"
    )
    @UniqueValue(fieldName = "email", entityClass = Student::class, message = "Email must be unique")
    val email: String?,

    @Column(name = "telegram", unique = true, nullable = true)
    @field:Pattern(
        regexp = "@[a-zA-Z_]+",
        message = "Telegram must be valid and begin with @"
    )
    @UniqueValue(fieldName = "telegram", entityClass = Student::class, message = "Telegram must be unique")
    val telegram: String?,

    @Column(name = "phone", unique = true, nullable = true)
    @Pattern(
        regexp = "\\+?[0-9]{10,15}",
        message = "Phone number must be valid and contain 10 to 15 digits"
    )
    @field:Pattern(
        regexp = "\\+?[0-9]{10,15}",
        message = "Phone number must be valid and contain 10 to 15 digits"
    )
    @UniqueValue(fieldName = "phone", entityClass = Student::class, message = "Phone number must be unique")
    val phone: String?,

    @Column(name = "git", unique = true, nullable = true)
    @UniqueValue(fieldName = "git", entityClass = Student::class, message = "Git must be unique")
    val git: String?,

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt: Date? = Date()
) {
    @PrePersist
    fun prePersist() {
        createdAt = Date() // Set the createdAt date only when the entity is persisted
    }
}
