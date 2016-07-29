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
 * We use front controller model to process and dispatch requests from users. Only
 * post method supported.
 *
 */
public class Controller extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/*
		 * To process request we need: 
		 * 1) DAO Layer implementation to work with stored data and record new. 
		 * 2) Validator implementation to verify data from requests. 
		 * 3) Commands factory to process different request options.
		 */
		DAOFactory factory = MySQLDAOFactory.getInstance();
		Validator validator = new DataValidator();

		String path = CompositeCommand.getInstance(factory, validator).execute(new HttpRequestContainer(request));
		request.getRequestDispatcher(path).forward(request, response);
	}
}
