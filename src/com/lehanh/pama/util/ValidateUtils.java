package com.lehanh.pama.util;

import java.security.InvalidParameterException;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

public class ValidateUtils {

	public static final boolean validateIsAllEmpty(String message, Object... objs) {
		for (Object obj : objs) {
			if ((obj instanceof String)) {
				if (!StringUtils.isBlank(((String) obj))) {
					return false;
				}
			} else if (obj instanceof Collection<?>) {
				if (obj != null && !((Collection<?>) obj).isEmpty()) {
					validateIsAllEmpty(message, ((Collection<?>) obj).toArray());
					return false;
				}
			} else if (obj != null) {
				return false;
			}
		}
		
		if (StringUtils.isBlank(message)) {
			return true;
		}
		throw new InvalidParameterException(message);
	}

}