package ua.kiral.project4.test.command;

import java.util.ResourceBundle;

public abstract class CommandsProperties {
	private static final ResourceBundle properties = ResourceBundle.getBundle("resources/commands");

	public static String getKey(String value) {
		return properties.getString(value);
	}
}
