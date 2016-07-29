package ua.kiral.project4.mock.dao;

import java.util.ArrayList;
import java.util.List;

import ua.kiral.project4.model.dao.UserDAO;
import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.User;

/**
 * Simple realization of UserDAO.
 *
 */
public class MockUserDAO implements UserDAO {

	@Override
	public boolean create(User entity) throws DAOException {
		if (entity == null)
			return false;

		return true;
	}

	@Override
	public boolean update(User oldEntity, User newEntity) throws DAOException {
		if (oldEntity == null || newEntity == null)
			return false;
		else
			return oldEntity.getUserId() > 0 ? true : false;
	}

	@Override
	public boolean delete(User entity) throws DAOException {
		return true;
	}

	@Override
	public List<User> getAll() throws DAOException {
		return new ArrayList<>();
	}

	@Override
	public User getById(Integer id) throws DAOException {
		if (id < 0)
			return new User(-1, "mock", "mock", "admin", "mock", "mock", false, false);
		else if (id == 0)
			return new User(1, "mock", "mock", "mock", "mock", "mock", false, true);
		else
			return new User(-1, "mock", "mock", "mock", "mock", "mock", false, false);
	}

	@Override
	public User getByLogin(String login) throws DAOException {
		switch (login) {
		case "admin":
			return new User(1, "mock", "mock", "admin", "mock", "mock", false, false);
		case "blocked":
			return new User(1, "mock", "mock", "mock", "mock", "mock", false, true);
		case "null":
			return null;
		case "anonym":
			return new User(-1, "mock", "mock", "mock", "mock", "mock", false, false);
		default:
			return new User(1, "mock", "mock", "mock", "mock", "mock", false, false);
		}
	}

	@Override
	public User getByEmail(String email) throws DAOException {
		if (email == null)
			return new User(-1, "mock", "mock", "mock", "mock", "mock", false, false);
		else
			return new User(1, "mock", "mock", "mock", "mock", "mock", false, false);
	}

	@Override
	public List<User> getByName(String name) throws DAOException {
		return name == null ? null : new ArrayList<>();
	}

	@Override
	public List<User> getBySurname(String surname) throws DAOException {
		return surname == null ? null : new ArrayList<>();
	}

	@Override
	public List<User> getByGender(boolean gender) throws DAOException {
		return gender == false ? null : new ArrayList<>();
	}

	@Override
	public List<User> getByStatus(boolean blocked) throws DAOException {
		return blocked == false ? null : new ArrayList<>();
	}

	@Override
	public boolean deleteByName(String name) throws DAOException {
		return name == null ? false : true;
	}

	@Override
	public boolean deleteBySurname(String surname) throws DAOException {
		return surname == null ? false : true;
	}

	@Override
	public boolean deleteByGender(boolean gender) throws DAOException {
		return gender == false ? false : true;
	}

	@Override
	public boolean deleteByStatus(boolean blocked) throws DAOException {
		return blocked == false ? false : true;
	}
}