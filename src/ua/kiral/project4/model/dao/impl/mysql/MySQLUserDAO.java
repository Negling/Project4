package ua.kiral.project4.model.dao.impl.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;

import ua.kiral.project4.model.dao.UserDAO;
import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.User;

public class MySQLUserDAO extends MySQLBaseDAO<User> implements UserDAO {

	public MySQLUserDAO(DataSource source) {
		super(source, "resources/dao/userSQL");
	}

	@Override
	public User getById(Integer id) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getSelectSQLWithKey("userId"))) {

			statement.setInt(1, id);
			return super.getEntityByQuerry(statement);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public User getByLogin(String login) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getSelectSQLWithKey("login"))) {

			statement.setString(1, login);
			return super.getEntityByQuerry(statement);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public User getByEmail(String email) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getSelectSQLWithKey("email"))) {

			statement.setString(1, email);
			return super.getEntityByQuerry(statement);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public List<User> getByName(String name) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getSelectSQLWithKey("name"))) {

			statement.setString(1, name);
			return super.getListByQuerry(statement);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public List<User> getBySurname(String surname) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getSelectSQLWithKey("surname"))) {

			statement.setString(1, surname);
			return super.getListByQuerry(statement);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public List<User> getByGender(boolean gender) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getSelectSQLWithKey("gender"))) {

			statement.setBoolean(1, gender);
			return super.getListByQuerry(statement);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public List<User> getByStatus(boolean blocked) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getSelectSQLWithKey("blacklist"))) {

			statement.setBoolean(1, blocked);
			return super.getListByQuerry(statement);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public List<User> getCustomersOnly() throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getSelectSQLWithKey("manager"))) {

			statement.setBoolean(1, false);
			return super.getListByQuerry(statement);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public List<User> getManagersOnly() throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getSelectSQLWithKey("manager"))) {

			statement.setBoolean(1, true);
			return super.getListByQuerry(statement);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public boolean deleteByName(String name) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement deleteStatement = connection.prepareStatement(getDeleteByKeySQL("name"))) {

			deleteStatement.setString(1, name);
			return transaction(connection, deleteStatement, -1);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public boolean deleteBySurname(String surname) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement deleteStatement = connection.prepareStatement(getDeleteByKeySQL("surname"))) {

			deleteStatement.setString(1, surname);
			return transaction(connection, deleteStatement, -1);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public boolean deleteByGender(boolean gender) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement deleteStatement = connection.prepareStatement(getDeleteByKeySQL("gender"))) {

			deleteStatement.setBoolean(1, gender);
			return transaction(connection, deleteStatement, -1);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public boolean deleteByStatus(boolean blocked) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement deleteStatement = connection.prepareStatement(getDeleteByKeySQL("blacklist"))) {

			deleteStatement.setBoolean(1, blocked);
			return transaction(connection, deleteStatement, -1);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	protected String getEntityExistanceSQL(User entity) {
		return queryData.getString("EntityExistanceSQL").replace("key!", entity.getLogin());
	}

	@Override
	protected void prepareStatementForInsert(PreparedStatement insertionStatement, User entity) throws DAOException {
		Boolean genderValue = entity.getMale();
		Boolean blockedValue = entity.getBlocked();
		Boolean managerValue = entity.getManager();
		String gender = genderValue == null ? null : (genderValue) ? "1" : "0";
		String blocked = blockedValue == null ? null : (blockedValue) ? "1" : "0";
		String manager = managerValue == null ? null : (managerValue) ? "1" : "0";

		try {
			insertionStatement.setString(1, entity.getName());
			insertionStatement.setString(2, entity.getSurname());
			insertionStatement.setString(3, entity.getLogin());
			insertionStatement.setString(4, entity.getPassword());
			insertionStatement.setString(5, entity.getEmail());
			insertionStatement.setString(6, gender);
			insertionStatement.setString(7, blocked);
			insertionStatement.setString(8, manager);
		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	protected void prepareStatementForUpdate(PreparedStatement updateStatement, User newEntity, User oldEntity)
			throws DAOException {
		Boolean genderValue = newEntity.getMale();
		Boolean blockedValue = newEntity.getBlocked();
		Boolean managerValue = newEntity.getManager();
		String gender = genderValue == null ? null : (genderValue) ? "1" : "0";
		String blocked = blockedValue == null ? null : (blockedValue) ? "1" : "0";
		String manager = managerValue == null ? null : (managerValue) ? "1" : "0";

		try {
			updateStatement.setString(1, newEntity.getName());
			updateStatement.setString(2, newEntity.getSurname());
			updateStatement.setString(3, newEntity.getLogin());
			updateStatement.setString(4, newEntity.getPassword());
			updateStatement.setString(5, newEntity.getEmail());
			updateStatement.setString(6, gender);
			updateStatement.setString(7, blocked);
			updateStatement.setString(8, manager);
			updateStatement.setString(9, oldEntity.getLogin());
		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	protected void prepareStatementForDelete(PreparedStatement deleteStatement, User entity) throws DAOException {
		try {
			deleteStatement.setString(1, entity.getLogin());
		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	protected User parseEntity(ResultSet rs) throws DAOException {
		try {
			// gender value can be null, it's equals to 'other' gender state
			Boolean gender = rs.getObject(7) == null ? null : rs.getBoolean(7);

			return new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
					rs.getString(6), gender, rs.getBoolean(8), rs.getBoolean(9));
		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}
}