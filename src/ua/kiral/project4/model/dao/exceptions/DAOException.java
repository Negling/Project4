package ua.kiral.project4.model.dao.exceptions;

/**
 * One <s>ring</s> Exception to rule them all! Provides interface to unite all
 * possible DAO layer exceptions to one-type exception processing.
 *
 */
public class DAOException extends Exception {

	public DAOException() {
	}

	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public DAOException(String message) {
		super(message);
	}

	public DAOException(Throwable cause) {
		super(cause);
	}
}