package ua.kiral.project4.model.dao;

import java.util.List;

import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.User;

/**
 * Specifies operations wich relate to User entity.
 *
 */
public interface UserDAO extends GenericDAO<User> {

	/**
	 * User wich mathes to specified id.
	 * 
	 * @param id
	 * @return null if there was no such user.
	 * @throws DAOException
	 */
	public User getById(Integer id) throws DAOException;

	/**
	 * User wich mathes to specified login;
	 * 
	 * @param login
	 * @return null if there was no such user.
	 * @throws DAOException
	 */
	public User getByLogin(String login) throws DAOException;

	/**
	 * User wich mathes to specified email.
	 * 
	 * @param email
	 * @return null if there was no such user.
	 * @throws DAOException
	 */
	public User getByEmail(String email) throws DAOException;

	/**
	 * Returns list of Users wich mathes to specified name.
	 * 
	 * @param name
	 * @return null if no users match to argument.
	 * @throws DAOException
	 */
	public List<User> getByName(String name) throws DAOException;

	/**
	 * Returns list of Users wich mathes to specified surname.
	 * 
	 * @param surname
	 * @return null if no users match to argument.
	 * @throws DAOException
	 */
	public List<User> getBySurname(String surname) throws DAOException;

	/**
	 * Returns list of Users wich mathes to specified gender.
	 * 
	 * @param gender
	 * @return null if no users match to argument.
	 * @throws DAOException
	 */
	public List<User> getByGender(boolean gender) throws DAOException;

	/**
	 * Returns list of Users wich mathes to specified status.
	 * 
	 * @param blocked
	 *            status
	 * @return null if no users match to argument.
	 * @throws DAOException
	 */
	public List<User> getByStatus(boolean blocked) throws DAOException;

	/**
	 * Returns list of customers only.
	 * 
	 * @return null if no users match to argument.
	 * @throws DAOException
	 */
	public List<User> getCustomersOnly() throws DAOException;

	/**
	 * Returns list of managers only.
	 * 
	 * @return null if no users match to argument.
	 * @throws DAOException
	 */
	public List<User> getManagersOnly() throws DAOException;

	/**
	 * Deletes all Users wich mathes to specified user name.
	 * 
	 * @param name
	 * @return <code>true</code> in case of success, false otherwise
	 * @throws DAOException
	 */
	public boolean deleteByName(String name) throws DAOException;

	/**
	 * Deletes all Users wich mathes to specified user surname;
	 * 
	 * @param surname
	 * @return <code>true</code> in case of success, false otherwise
	 * @throws DAOException
	 */
	public boolean deleteBySurname(String surname) throws DAOException;

	/**
	 * Deletes all Users wich mathes to specified user gender.
	 * 
	 * @param gender
	 * @return <code>true</code> in case of success, false otherwise
	 * @throws DAOException
	 */
	public boolean deleteByGender(boolean gender) throws DAOException;

	/**
	 * Deletes all Users wich mathes to specified blocked status.
	 * 
	 * @param status
	 * @return <code>true</code> in case of success, false otherwise
	 * @throws DAOException
	 */
	public boolean deleteByStatus(boolean blocked) throws DAOException;
}