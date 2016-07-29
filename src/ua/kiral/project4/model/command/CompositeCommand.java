package ua.kiral.project4.model.command;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.kiral.project4.controller.request.RequestContainer;
import ua.kiral.project4.model.command.validator.Validator;
import ua.kiral.project4.model.dao.DAOFactory;

/**
 * Represents set of commands pull, and by invoking "execute" method delegates
 * task to one of them.
 *
 */
public final class CompositeCommand extends Command {
	final static Logger logger = LogManager.getLogger(CompositeCommand.class);
	private HashMap<String, Command> commands;

	private CompositeCommand(DAOFactory factory, Validator validator) {
		super(factory, validator);
		this.commands = new HashMap<>();
	}

	/**
	 * Invokes specified command and returns result of it's service. Command to
	 * invoke is specified by name field, passed from request.
	 */
	@Override
	public String execute(RequestContainer container) {
		String commandKey = container.getParameter(getKey("command"));

		if (commandKey != null) {

			String commandName = getKey(commandKey);

			/*
			 * If commands pool has no instance bounded to argument, we will
			 * create one by reflection and put it there.
			 */
			if (!commands.containsKey(commandName)) {

				try {
					commands.put(commandName, getCommand(DAOfactory, validator, commandName));
				} catch (ReflectiveOperationException exeption) {

					logger.error("Exeption via attemp to create Command by reflextion", exeption);
					return getKey("errorPath");
				}
			}

			return commands.get(commandName).execute(container);

		} else
			return getKey("errorPath");
	}

	/**
	 * Means reflection creates instance of Command, which name matches to
	 * argument prefix. This is part of so called lazy initialization
	 * implementation.
	 * 
	 * @param factoryInstance
	 * @param validatorInstance
	 * @param commandName
	 * @return
	 * @throws ReflectiveOperationException
	 */
	private Command getCommand(DAOFactory factoryInstance, Validator validatorInstance, String commandName)
			throws ReflectiveOperationException {
		Class<?> clazz = Class.forName("ua.kiral.project4.model.command.impl." + commandName + "Command");
		Constructor<?> constructor = clazz.getConstructor(DAOFactory.class, Validator.class);

		return (Command) constructor.newInstance(factoryInstance, validatorInstance);
	}

	/**
	 * Returns composite instance wich specified by DAO factory and Validator
	 * instance.
	 * 
	 * @param factory
	 * @param validator
	 * @return
	 */
	public static CompositeCommand getInstance(DAOFactory factory, Validator validator) {
		return new CompositeCommand(factory, validator);
	}
}