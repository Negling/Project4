package ua.kiral.project4.model.command.impl;

import ua.kiral.project4.controller.request.RequestContainer;
import ua.kiral.project4.model.command.Command;
import ua.kiral.project4.model.command.validation.Validator;
import ua.kiral.project4.model.dao.DAOFactory;

/**
 * Invalidates session, throws user to main page. Nuff said.
 *
 */
public class LogoutCommand extends Command {

	public LogoutCommand(DAOFactory factory, Validator validator) {
		super(factory, validator);
	}

	@Override
	public String execute(RequestContainer container) {
		container.getSession(true).invalidate();
		return getKey("mainPage");
	}
}