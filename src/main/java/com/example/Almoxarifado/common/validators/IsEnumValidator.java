package com.example.Almoxarifado.common.validators;

import java.util.List;
import java.util.stream.Stream;

import com.example.Almoxarifado.common.typeValidations.IsEnum;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsEnumValidator implements ConstraintValidator<IsEnum, CharSequence>{
    private List<String> valoresAceitos;

    @Override
    public void initialize(IsEnum annotation) {
        valoresAceitos = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .toList();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        
        return valoresAceitos.contains(value.toString());
    }
}
