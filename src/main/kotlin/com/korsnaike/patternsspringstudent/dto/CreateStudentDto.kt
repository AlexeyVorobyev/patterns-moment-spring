package com.korsnaike.patternsspringstudent.dto

import com.korsnaike.patternsspringstudent.entity.Student
import com.korsnaike.patternsspringstudent.utils.VALID_EMAIL_ADDRESS_REGEX_WITH_EMPTY_SPACES_ACCEPTANCE
import com.korsnaike.patternsspringstudent.validators.unique.UniqueValue
import jakarta.validation.constraints.*

data class CreateStudentDto(
    @field:NotBlank(message = "First name must not be blank")
    val firstName: String? = null,

    @field:NotBlank(message = "Last name must not be blank")
    val lastName: String? = null,

    @field:NotBlank(message = "Middle name must not be blank")
    val middleName: String? = null,

    @field:Email(message = "Not correct format")
    @field:Pattern(
        regexp = VALID_EMAIL_ADDRESS_REGEX_WITH_EMPTY_SPACES_ACCEPTANCE,
        message = "Not correct format"
    )
    @UniqueValue(fieldName = "email", entityClass = Student::class, message = "Email must be unique")
    val email: String? = null,

    @field:Pattern(
        regexp = "@[a-zA-Z_]+",
        message = "Telegram must be valid and begin with @"
    )
    @UniqueValue(fieldName = "telegram", entityClass = Student::class, message = "Telegram must be unique")
    val telegram: String? = null,

    @field:Pattern(
        regexp = "\\+?[0-9]{10,15}",
        message = "Phone number must be valid and contain 10 to 15 digits"
    )
    @UniqueValue(fieldName = "phone", entityClass = Student::class, message = "Phone number must be unique")
    val phone: String? = null,

    @UniqueValue(fieldName = "git", entityClass = Student::class, message = "Git must be unique")
    val git: String? = null
) {

    fun toEntity(): Student = Student(
        firstName = this.firstName ?: "",
        lastName = this.lastName?: "",
        middleName = this.middleName?: "",
        email = this.email,
        telegram = this.telegram,
        phone = this.phone,
        git = this.git
    )
}
