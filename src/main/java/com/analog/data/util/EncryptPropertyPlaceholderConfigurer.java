package com.analog.data.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptPropertyPlaceholderConfigurer extends
		PropertyPlaceholderConfigurer {
	//private String[] encryptPropNames = { "jdbc.username", "jdbc.password" };
	//TODO 对账号密码进行加密,暂时不做处理
	private String[] encryptPropNames = { "jdbc.username1", "jdbc.password1" };

	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		if (isEncryptProp(propertyName)) {
			String decryptValue = DESUtils.getDecryptString(propertyValue);
			return decryptValue;
		} else {
			return propertyValue;
		}
	}

	private boolean isEncryptProp(String propertyName) {
		for (String encryptpropertyName : encryptPropNames) {
			if (encryptpropertyName.equals(propertyName))
				return true;
		}
		return false;
	}
}
