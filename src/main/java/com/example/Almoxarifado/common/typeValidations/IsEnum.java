package com.example.Almoxarifado.common.typeValidations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.Almoxarifado.common.validators.IsEnumValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = IsEnumValidator.class)
public @interface IsEnum {
    
    Class<? extends Enum<?>> enumClass();
    String message() default "O valor informado não é válido para este campo.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
