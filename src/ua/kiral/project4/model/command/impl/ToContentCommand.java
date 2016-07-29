package ua.kiral.project4.model.command.impl;

/**
 * Simply passes to content page. Necessity of security.
 */
import ua.kiral.project4.controller.request.RequestContainer;
import ua.kiral.project4.model.command.Command;
import ua.kiral.project4.model.command.validator.Validator;
import ua.kiral.project4.model.dao.DAOFactory;

public class ToContentCommand extends Command {

	public ToContentCommand(DAOFactory factory, Validator validator) {
		super(factory, validator);
	}

	@Override
	public String execute(RequestContainer container) {
		return getKey("contentPath");
	}
}