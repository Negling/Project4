package ua.kiral.project4.model.dao.impl.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;

import ua.kiral.project4.model.dao.OrderDetailsDAO;
import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.OrderDetails;

public class MySQLOrderDetailsDAO extends MySQLBaseDAO<OrderDetails> implements OrderDetailsDAO {

	public MySQLOrderDetailsDAO(DataSource source) {
		super(source, "resources/dao/order_detailsSQL");
	}

	@Override
	public boolean create(OrderDetails entity) throws DAOException {
		if (entity == null)
			throw new IllegalArgumentException("Entity can't be null!");

		// Check that newEntity ain't exist in DB.
		if (checkExistence(entity))
			return false;

		int rowstoBeAffected = entity.getProducts().size();
		String commandBatch[] = new String[rowstoBeAffected];
		String batchElement;

		if (rowstoBeAffected == 0)
			return false;

		int batchIndex = 0;
		// Initializing batch with querry SQL.
		for (Integer productId : entity.getProducts().keySet()) {

			// order of replacement is important
			batchElement = getInsertionSQL();
			batchElement = batchElement.replaceFirst("\\?", entity.getOrderId().toString());
			batchElement = batchElement.replaceFirst("\\?", productId.toString());
			batchElement = batchElement.replaceFirst("\\?", entity.getProductAmount(productId).toString());

			commandBatch[batchIndex] = batchElement;

			batchIndex++;
		}
		try (Connection connection = getConnection()) {
			return transaction(connection, commandBatch, rowstoBeAffected);
		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public OrderDetails getByOrderId(int orderId) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getSelectSQLWithKey("orderId"))) {

			statement.setInt(1, orderId);
			return super.getEntityByQuerry(statement);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public boolean update(OrderDetails oldEntity, OrderDetails newEntity) throws DAOException {
		if (oldEntity == null || newEntity == null)
			throw new IllegalArgumentException("Entity can't be null!");
		if (oldEntity.getProducts().size() != newEntity.getProducts().size())
			throw new IllegalArgumentException("Objects rows amount are not equal!");
		if (!checkExistence(oldEntity))
			return false;

		int rowstoBeAffected = newEntity.getProducts().size();
		String commandBatch[] = new String[rowstoBeAffected];
		String batchElement;

		if (rowstoBeAffected == 0)
			return false;

		int batchIndex = 0;
		// Initializing batch with querry SQL.
		Iterator<Integer> newKeys = newEntity.getProducts().keySet().iterator();
		Iterator<Integer> oldKeys = oldEntity.getProducts().keySet().iterator();

		while (newKeys.hasNext() && oldKeys.hasNext()) {
			Integer newKey = newKeys.next();
			Integer oldKey = oldKeys.next();

			// order of replacement is important
			batchElement = getUpdateSQL();
			batchElement = batchElement.replaceFirst("\\?", newEntity.getOrderId().toString());
			batchElement = batchElement.replaceFirst("\\?", newKey.toString());
			batchElement = batchElement.replaceFirst("\\?", newEntity.getProductAmount(newKey).toString());
			batchElement = batchElement.replaceFirst("\\?", oldEntity.getOrderId().toString());
			batchElement = batchElement.replaceFirst("\\?", oldKey.toString());

			commandBatch[batchIndex] = batchElement;

			batchIndex++;
		}

		try (Connection connection = getConnection()) {
			return transaction(connection, commandBatch, rowstoBeAffected);
		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public boolean delete(OrderDetails entity) throws DAOException {
		return deleteAllByOrderId(entity.getOrderId());
	}

	@Override
	public boolean deleteAllByOrderId(int orderId) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement deleteStatement = connection.prepareStatement(getDeleteSQL())) {

			deleteStatement.setInt(1, orderId);
			return transaction(connection, deleteStatement, -1);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	protected String getNonOrderedSelectSQL() {
		return queryData.getString("SelectNonOrderedSQL");
	}

	@Override
	protected String getEntityExistanceSQL(OrderDetails entity) {
		return queryData.getString("EntityExistanceSQL") + entity.getOrderId();
	}

	@Override
	protected void prepareStatementForInsert(PreparedStatement insertionStatement, OrderDetails entity)
			throws DAOException {
		// Does nothing, parent method 'create' overrided
	}

	@Override
	protected void prepareStatementForUpdate(PreparedStatement updateStatement, OrderDetails newEntity,
			OrderDetails oldEntity) throws DAOException {
		// Does nothing, parent method 'update' overrided
	}

	@Override
	protected void prepareStatementForDelete(PreparedStatement deleteStatement, OrderDetails entity)
			throws DAOException {
		try {
			deleteStatement.setInt(1, entity.getOrderId());
		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	protected OrderDetails parseEntity(ResultSet rs) throws DAOException {
		try {
			OrderDetails result = new OrderDetails(rs.getInt(1), new HashMap<>());

			result.putProduct(rs.getInt(2), rs.getInt(3));

			while (rs.next()) {
				if (rs.getInt(1) != result.getOrderId()) {
					rs.previous();
					break;
				}
				result.putProduct(rs.getInt(2), rs.getInt(3));
			}

			return result;
		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	protected List<OrderDetails> parseResultSet(ResultSet rs) throws DAOException {
		List<OrderDetails> result = new ArrayList<>();
		OrderDetails parsedEntity = parseEntity(rs);

		while (parsedEntity != null) {
			result.add(parsedEntity);
			parsedEntity = parseEntity(rs);
		}
		return result;
	}
}