package com.example.easy.commons.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.ArrayUtils;

public class AllowedValuesValidator implements ConstraintValidator<AllowedValues, Object>
{
	/**
	 * String array of possible enum values
	 */
	private String[] values;

	@Override
	public void initialize(final AllowedValues constraintAnnotation)
	{
		this.values = constraintAnnotation.values();
	}

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext context)
	{
		if (value == null)
		{
			return true;
		}
		return ArrayUtils.contains(this.values, value == null ? null : value.toString());
	}
}
