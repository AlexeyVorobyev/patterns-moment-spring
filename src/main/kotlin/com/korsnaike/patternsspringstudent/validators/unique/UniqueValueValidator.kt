package com.korsnaike.patternsspringstudent.validators.unique

import com.korsnaike.patternsspringstudent.entity.Student
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.stereotype.Component
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext

@Component
class UniqueValueValidator : ConstraintValidator<UniqueValue, Any> {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private lateinit var fieldName: String
    private lateinit var entityClass: Class<*>

    override fun initialize(constraintAnnotation: UniqueValue) {
        fieldName = constraintAnnotation.fieldName
        entityClass = constraintAnnotation.entityClass.java
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }

        // Извлекаем корневой объект, который валидируется
        val rootBean = context.unwrap(org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext::class.java) as? Student

        val currentId = rootBean?.id

        if (!::entityManager.isInitialized) {
            return true
        }

        val query = entityManager.createQuery(
            """
        SELECT COUNT(e) 
        FROM ${entityClass.simpleName} e 
        WHERE e.$fieldName = :value 
        AND (:id IS NULL OR e.id != :id)
        """
        )
        query.setParameter("value", value)
        query.setParameter("id", currentId)

        val count = query.singleResult as Long
        return count == 0L
    }
}
