package com.korsnaike.patternsspringstudent.dto

import com.korsnaike.patternsspringstudent.utils.VALID_EMAIL_ADDRESS_REGEX_WITH_EMPTY_SPACES_ACCEPTANCE
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class GetAllStudentsQueryParamsDto(
    val firstName: String? = null,

    val lastName: String? = null,

    val middleName: String? = null,

    @field:Pattern(regexp = VALID_EMAIL_ADDRESS_REGEX_WITH_EMPTY_SPACES_ACCEPTANCE, message = "Invalid email format")
    val email: String? = null,

    val telegram: String? = null,

    @field:Pattern(regexp = "\\+?[0-9]{10,15}", message = "Invalid phone number format")
    val phone: String? = null,

    val git: String? = null,

    @field:Min(value = 0, message = "Page number must be 0 or greater")
    val page: Int = 0,

    @field:Min(value = 1, message = "Page size must be at least 1")
    @field:Max(value = 100, message = "Page size must not exceed 100")
    val size: Int = 10,

    @field:NotBlank(message = "SortBy cannot be empty")
    val sortBy: String = "id",

    @field:Pattern(regexp = "ASC|DESC", message = "Direction must be 'ASC' or 'DESC'")
    val direction: String = "ASC"
)
