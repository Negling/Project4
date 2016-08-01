package ua.kiral.project4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.kiral.project4.controller.request.HttpRequestContainer;
import ua.kiral.project4.model.command.CompositeCommand;
import ua.kiral.project4.model.command.validator.DataValidator;
import ua.kiral.project4.model.command.validator.Validator;
import ua.kiral.project4.model.dao.DAOFactory;
import ua.kiral.project4.model.dao.impl.mysql.MySQLDAOFactory;

/**
 * We use front controller model to process and dispatch requests from users.
 * Only post method supported.
 *
 */
public class Controller extends HttpServlet {
	private CompositeCommand commands;

	@Override
	public void init() throws ServletException {
		/*
		 * To process request we need Command imlementation to invoke procedures
		 * appropriate to request options. Commands globally dependens on DAO
		 * layer implementation, and Validator implementation. In composite
		 * command we store commands bound to each layer specified variables.
		 * 
		 */
		DAOFactory factory = MySQLDAOFactory.getInstance();
		Validator validator = new DataValidator();
		commands = CompositeCommand.getInstance(factory, validator);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// execute necessary command and forward
		String path = commands.execute(new HttpRequestContainer(request));
		request.getRequestDispatcher(path).forward(request, response);
	}
}