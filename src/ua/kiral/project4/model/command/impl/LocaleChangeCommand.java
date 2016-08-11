package ua.kiral.project4.model.command.impl;

import javax.servlet.http.HttpSession;

import ua.kiral.project4.controller.request.RequestContainer;
import ua.kiral.project4.model.command.Command;
import ua.kiral.project4.model.command.validation.Validator;
import ua.kiral.project4.model.dao.DAOFactory;

/**
 * Simple command, replaces old locale value by new from request, and returns
 * page path back.
 *
 */
public class LocaleChangeCommand extends Command {

	public LocaleChangeCommand(DAOFactory factory, Validator validator) {
		super(factory, validator);
	}

	@Override
	public String execute(RequestContainer container) {
		HttpSession session = container.getSession(true);
		
		// changing locale value to value from request
		session.setAttribute(getKey("locale"), container.getParameter(getKey("locale")));
		
		// return back to page
		return container.getParameter(getKey("requestPath"));
	}
}
