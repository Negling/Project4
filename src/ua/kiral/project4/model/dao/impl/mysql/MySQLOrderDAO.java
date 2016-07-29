package ua.kiral.project4.model.dao.impl.mysql;

import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;

import ua.kiral.project4.model.dao.OrderDAO;
import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.Order;

public class MySQLOrderDAO extends MySQLBaseDAO<Order> implements OrderDAO {

	public MySQLOrderDAO(DataSource source) {
		super(source, "resources/dao/orderSQL");
	}

	@Override
	public int getOrderId(Order order) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getIdSQL())) {

			statement.setInt(1, order.getOwnerId());
			statement.setDate(2, new Date(order.getOrderDate().getTime()));
			statement.setString(3, order.getStatus());
			statement.setBigDecimal(4, order.getTotalCost());
			
			Order result = super.getEntityByQuerry(statement);
			
			if(result==null)
				return -1;
			else
				return result.getOrderId();

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public Order getOrderByID(Integer orderId) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getSelectSQLWithKey("orderId"))) {

			statement.setInt(1, orderId);
			return super.getEntityByQuerry(statement);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public List<Order> getAllByStatus(String status) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getSelectSQLWithKey("status"))) {

			statement.setString(1, status);
			return super.getListByQuerry(statement);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public List<Order> getAllByOwner(Integer ownerId) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getSelectSQLWithKey("ownerId"))) {

			statement.setInt(1, ownerId);
			return super.getListByQuerry(statement);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public List<Order> getAllByDate(java.util.Date date) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getSelectSQLWithKey("orderDate"))) {

			statement.setDate(1, new Date(date.getTime()));
			return super.getListByQuerry(statement);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public boolean deleteAllByStatus(String status) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement deleteStatement = connection.prepareStatement(getDeleteByKeySQL("status"))) {

			deleteStatement.setString(1, status);
			return transaction(connection, deleteStatement, -1);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public boolean deleteAllByOwner(Integer ownerId) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement deleteStatement = connection.prepareStatement(getDeleteByKeySQL("ownerLogin"));) {

			deleteStatement.setInt(1, ownerId);
			return transaction(connection, deleteStatement, -1);
			
		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public boolean deleteAllByDate(java.util.Date date) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement deleteStatement = connection.prepareStatement(getDeleteByKeySQL("orderDate"));) {

			deleteStatement.setDate(1, new Date(date.getTime()));
			return transaction(connection, deleteStatement, -1);
			
		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	protected String getEntityExistanceSQL(Order entity) {
		return queryData.getString("EntityExistanceSQL") + entity.getOrderId();
	}
	
	protected String getIdSQL(){
		return queryData.getString("IdSQL");
	}

	@Override
	protected void prepareStatementForInsert(PreparedStatement insertionStatement, Order entity) throws DAOException {
		try {
			insertionStatement.setInt(1, entity.getOwnerId());
			insertionStatement.setDate(2, new Date(entity.getOrderDate().getTime()));
			insertionStatement.setString(3, entity.getStatus());
			insertionStatement.setBigDecimal(4, entity.getTotalCost());
		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	protected void prepareStatementForUpdate(PreparedStatement updateStatement, Order newEntity, Order oldEntity)
			throws DAOException {
		try {
			updateStatement.setInt(1, newEntity.getOwnerId());
			updateStatement.setDate(2, new Date(newEntity.getOrderDate().getTime()));
			updateStatement.setString(3, newEntity.getStatus());
			updateStatement.setBigDecimal(4, newEntity.getTotalCost());
			updateStatement.setInt(5, oldEntity.getOrderId());
		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	protected void prepareStatementForDelete(PreparedStatement deleteStatement, Order entity) throws DAOException {
		try {
			deleteStatement.setInt(1, entity.getOrderId());
		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	protected Order parseEntity(ResultSet rs) throws DAOException {
		try {
			return new Order(rs.getInt(1), rs.getInt(2), rs.getDate(3), rs.getString(4),
					rs.getBigDecimal(5).setScale(2, RoundingMode.HALF_EVEN));
		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}
}