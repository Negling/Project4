package ua.kiral.project4.model.dao.impl.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.tomcat.jdbc.pool.DataSource;

import ua.kiral.project4.model.dao.GenericDAO;
import ua.kiral.project4.model.dao.exceptions.DAOException;

/**
 * Core class for MySQL DAO realization. Implements most of all main operations
 * with DB.
 *
 * @param <E>
 */
abstract class MySQLBaseDAO<E> implements GenericDAO<E> {
	private DataSource source;
	protected ResourceBundle queryData;

	public MySQLBaseDAO(DataSource source, String bundlename) {
		this.source = source;
		this.queryData = ResourceBundle.getBundle(bundlename);
	}

	@Override
	public boolean create(E entity) throws DAOException {
		if (entity == null)
			throw new IllegalArgumentException("Entity can't be null!");

		// Check that newEntity ain't exist in DB.
		if (checkExistence(entity))
			return false;

		try (Connection connection = source.getConnection();
				PreparedStatement insertionStatement = connection.prepareStatement(getInsertionSQL())) {

			// Prepearing data to execute by setting entity info to query.
			prepareStatementForInsert(insertionStatement, entity);

			// Execute transaction.
			return transaction(connection, insertionStatement, 1);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public boolean update(E oldEntity, E newEntity) throws DAOException {
		if (oldEntity == null || newEntity == null)
			throw new IllegalArgumentException("Entity can't be null!");

		// Check that oldEntity is actualy exist in DB.
		if (!checkExistence(oldEntity))
			return false;

		try (Connection connection = source.getConnection();
				PreparedStatement updateStatement = connection.prepareStatement(getUpdateSQL())) {

			// Prepearing data to execute by setting entity info to query.
			prepareStatementForUpdate(updateStatement, newEntity, oldEntity);

			// Execute transaction.
			return transaction(connection, updateStatement, 1);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public boolean delete(E entity) throws DAOException {
		if (entity == null)
			throw new IllegalArgumentException("Entity can't be null!");

		// Check that entity is actualy exist in DB.
		if (!checkExistence(entity))
			return false;

		try (Connection connection = source.getConnection();
				PreparedStatement deleteStatement = connection.prepareStatement(getDeleteSQL())) {

			// Prepearing data to execute by setting entity info to query.
			prepareStatementForDelete(deleteStatement, entity);

			// Execute transaction.
			return transaction(connection, deleteStatement, 1);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public List<E> getAll() throws DAOException {
		final List<E> data;

		try (Connection connection = source.getConnection();
				PreparedStatement st = connection.prepareStatement(getSelectSQL());
				ResultSet rs = st.executeQuery()) {

			data = parseResultSet(rs);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
		if (data.isEmpty())
			return null;
		else
			return data;
	}

	/**
	 * Returns connection from data source.
	 * 
	 * @return
	 * @throws DAOException
	 */
	protected Connection getConnection() throws DAOException {
		try {
			return source.getConnection();
		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	/**
	 * Provides simple transaction interface. Operation has 1 attempt to
	 * success, otherwise considered as failure. If records that transaction
	 * modified does'nt match to expected value - transaction considered as
	 * failure, if value of modified rows unnecessary(affects 1 and more rows) -
	 * set expectedModificationCount param as -1.
	 * 
	 * @param statement
	 *            - PreparedStatement to execute.
	 * @param expectedModificationCount
	 *            - expected amount of rows that transaction will modify.
	 * @return <code>true</code> in case of success transaction,
	 *         <code>false</code> otherwise.
	 * @throws DAOException
	 */
	protected boolean transaction(Connection connection, PreparedStatement statement, int expectedModificationCount)
			throws DAOException {
		int affectedRows;

		try {
			// executing operation.
			affectedRows = statement.executeUpdate();

			
			/*
			 * if affected rows is necessary value, and its value doesn't match
			 * to expected - throw exception.
			 */
			if (expectedModificationCount != -1 && affectedRows != expectedModificationCount) {
				throw new SQLException();
			} else if (affectedRows < 1) {
				throw new SQLException();
			}

			// in case of successful operation - commit.
			connection.commit();

			// at last - close statement and exit loop.
			statement.close();
			return true;
		} catch (SQLException ignore) {

			// if operation failed - try to rollback.
			try {
				connection.rollback();
			} catch (SQLException cause) {
				throw new DAOException(cause);
			}
		}
		return false;
	}

	/**
	 * Provides simple transaction interface. Operation has 1 attempt to
	 * success, otherwise considered as failure. If records that transaction
	 * modified does'nt match to expected value - transaction considered as
	 * failure, if value of modified rows unnecessary(affects 1 and more rows) -
	 * set expectedModificationCount param as -1.
	 * 
	 * @param commandsBatch
	 *            - array of commands batch to execute.
	 * @param expectedModificationCount
	 *            - expected amount of rows that transaction will modify.
	 * @return <code>true</code> in case of success transaction,
	 *         <code>false</code> otherwise.
	 * @throws DAOException
	 */
	protected boolean transaction(Connection connection, String[] commandsBatch, int expectedModificationCount)
			throws DAOException {
		Statement statement;
		int affectedRows;
		int batchResults[];

		// Creating statement and adding batch of commantds to it.
		try {
			statement = connection.createStatement();

			for (String batch : commandsBatch)
				statement.addBatch(batch);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}

		// main transaction logic
		try {
			// executing operation.
			batchResults = statement.executeBatch();

			// zero out rows value
			affectedRows = 0;

			// counting affected rows;
			for (int operationResult : batchResults) {
				if (operationResult > 0)
					affectedRows += operationResult;
			}

			/*
			 * if affected rows is necessary value, and its value doesn't match
			 * to expected - throw exception.
			 */
			if (expectedModificationCount != -1 && affectedRows != expectedModificationCount) {
				throw new SQLException();
			}

			// in case of successful operation - commit.
			connection.commit();

			// at last - close statement and exit loop.
			statement.close();
			return true;
		} catch (SQLException ignore) {

			// if operation failed - try to rollback.
			try {
				connection.rollback();
			} catch (SQLException cause) {
				throw new DAOException(cause);
			}
		}
		return false;
	}

	/**
	 * Returns list of objects witch match to specified querry.
	 * 
	 * @param querry
	 * @return null if no records match to querry.
	 * @throws DAOException
	 */
	protected List<E> getListByQuerry(PreparedStatement statement) throws DAOException {
		final List<E> data;

		try (ResultSet rs = statement.executeQuery()) {
			data = parseResultSet(rs);
		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
		if (data.isEmpty())
			return null;
		else
			return data;
	}

	/**
	 * Returns first object witch match to specified querry.
	 * 
	 * @param querry
	 * @return null if no records match to querry.
	 * @throws DAOException
	 */
	protected E getEntityByQuerry(PreparedStatement statement) throws DAOException {
		E result = null;

		try (ResultSet rs = statement.executeQuery()) {

			if (rs.next())
				result = parseEntity(rs);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
		return result;
	}

	/**
	 * Returns true if entity exist in DB, and false otherwise.
	 * 
	 * @param entity
	 * @return
	 * @throws DAOException
	 */
	protected boolean checkExistence(E entity) throws DAOException {
		String existanceSQL = getEntityExistanceSQL(entity);

		try (Connection connection = source.getConnection();
				Statement st = connection.createStatement();
				ResultSet rs = st.executeQuery(existanceSQL)) {

			return rs.next();

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	/**
	 * Returns specified entity parsed from result set.
	 * 
	 * @param rs
	 * @return Entity specified by subclass
	 * @throws DAOException
	 */
	protected abstract E parseEntity(ResultSet rs) throws DAOException;

	/**
	 * Returns list specified by subclass entity.
	 * 
	 * @param rs
	 * @return
	 * @throws DAOException
	 */
	protected List<E> parseResultSet(ResultSet rs) throws DAOException {
		List<E> result = new ArrayList<>();

		try {
			while (rs.next()) {
				result.add(parseEntity(rs));
			}
		} catch (SQLException cause) {
			throw new DAOException(cause);
		}

		return result;
	}

	/**
	 * Returns prepared SQL Select string.
	 * 
	 * @return
	 */
	protected String getSelectSQL() {
		return queryData.getString("SelectSQL");
	}

	/**
	 * Returns prepared SQL Select string with special 'key' value in some place
	 * of string.
	 * 
	 * @return
	 */
	protected String getSelectSQLWithKey(String key) {
		return queryData.getString("SelectSQLWithKeySQL").replace("key!", key);
	}

	/**
	 * Returns prepared SQL Insert string.
	 * 
	 * @return
	 */
	protected String getInsertionSQL() {
		return queryData.getString("InsertionSQL");
	}

	/**
	 * Returns prepared SQL Update string.
	 * 
	 * @return
	 */
	protected String getUpdateSQL() {
		return queryData.getString("UpdateSQL");
	}

	/**
	 * Returns prepared SQL Delete string.
	 * 
	 * @return
	 */
	protected String getDeleteSQL() {
		return queryData.getString("DeleteSQL");
	}

	/**
	 * Returns prepared SQL Delete string with special 'key' value in some place
	 * of string.
	 * 
	 * @return
	 */
	protected String getDeleteByKeySQL(String key) {
		return queryData.getString("DeleteByKeySQL").replace("key!", key);
	}

	/**
	 * Returns prepared SQL select string with prepared parameter.
	 * 
	 * @return
	 */
	protected abstract String getEntityExistanceSQL(E entity);

	/**
	 * Prepears statement for insertion. Long story short, just sets specified
	 * intity values to '?' signs.
	 * 
	 * @param insertionStatement
	 * @param entity
	 * @throws DAOException
	 */
	protected abstract void prepareStatementForInsert(PreparedStatement insertionStatement, E entity)
			throws DAOException;

	/**
	 * Prepears statement for update.
	 * 
	 * @param updateStatement
	 * @param newEntity
	 * @param oldEntity
	 * @throws DAOException
	 */
	protected abstract void prepareStatementForUpdate(PreparedStatement updateStatement, E newEntity, E oldEntity)
			throws DAOException;

	/**
	 * Prepears statement for delete.
	 * 
	 * @param deleteStatement
	 * @param entity
	 * @throws DAOException
	 */
	protected abstract void prepareStatementForDelete(PreparedStatement deleteStatement, E entity) throws DAOException;
}