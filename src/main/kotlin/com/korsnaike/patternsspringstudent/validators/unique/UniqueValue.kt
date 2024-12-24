package com.korsnaike.patternsspringstudent.validators.unique

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UniqueValueValidator::class])
annotation class UniqueValue(
    val message: String = "Value must be unique",
    val fieldName: String,
    val entityClass: KClass<*>,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
