package com.gerrywen.seckill.validator;

import com.gerrywen.seckill.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * program: spring-boot-seckill->IsMobileValidator
 * description:
 * author: gerry
 * created: 2020-03-05 20:41
 **/
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {
    private boolean required = false;

    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (required) {
            return ValidatorUtil.isMobile(value);
        } else {
            if (StringUtils.isEmpty(value)) {
                return true;
            } else {
                return ValidatorUtil.isMobile(value);
            }
        }
    }
}
