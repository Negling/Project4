package ua.kiral.project4.model.dao.impl.mysql;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;
import ua.kiral.project4.model.dao.ProductDAO;
import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.Order;
import ua.kiral.project4.model.entity.Product;

public class MySQLProductDAO extends MySQLBaseDAO<Product> implements ProductDAO {

	public MySQLProductDAO(DataSource source) {
		super(source, "resources/dao/productSQL");
	}

	@Override
	public int getProductId(Product product) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getIdSQL())) {

			statement.setString(1, product.getName());
			statement.setBigDecimal(2, product.getPrice());
			Product result = super.getEntityByQuerry(statement);

			if (result == null)
				return -1;
			else
				return result.getId();

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public Product getProductByID(int productId) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getSelectSQLWithKey("productId ="))) {

			statement.setInt(1, productId);
			return super.getEntityByQuerry(statement);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public List<Product> getAllByName(String name) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getSelectSQLWithKey("name ="))) {

			statement.setString(1, name);
			return super.getListByQuerry(statement);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public List<Product> getAllByStatus(Boolean status) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getSelectSQLWithKey("removed ="))) {

			statement.setBoolean(1, status);
			return super.getListByQuerry(statement);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public List<Product> getLower(BigDecimal price) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getSelectSQLWithKey("price <"))) {

			statement.setBigDecimal(1, price);
			return super.getListByQuerry(statement);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public List<Product> getLowerAndEquals(BigDecimal price) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getSelectSQLWithKey("price <="))) {

			statement.setBigDecimal(1, price);
			return super.getListByQuerry(statement);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public List<Product> getGreater(BigDecimal price) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getSelectSQLWithKey("price >"))) {

			statement.setBigDecimal(1, price);
			return super.getListByQuerry(statement);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public List<Product> getGreaterAndEquals(BigDecimal price) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getSelectSQLWithKey("price >="))) {

			statement.setBigDecimal(1, price);
			return super.getListByQuerry(statement);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public List<Product> getProductsFromOrder(Order order) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(getJoinSQL())) {

			statement.setInt(1, order.getOrderId());
			return super.getListByQuerry(statement);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public boolean deleteById(int productID) throws DAOException {
		return super.delete(new Product(productID, "", new BigDecimal(0), false));
	}

	@Override
	public boolean deleteAllByName(String name) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement deleteStatement = connection.prepareStatement(getDeleteByKeySQL("name"))) {

			deleteStatement.setString(1, name);
			return transaction(connection, deleteStatement, -1);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	public boolean deleteAllByPrice(BigDecimal price) throws DAOException {
		try (Connection connection = getConnection();
				PreparedStatement deleteStatement = connection.prepareStatement(getDeleteByKeySQL("price"))) {

			deleteStatement.setBigDecimal(1, price);
			return transaction(connection, deleteStatement, -1);

		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	protected String getEntityExistanceSQL(Product entity) {
		return queryData.getString("EntityExistanceSQL") + entity.getId();
	}

	protected String getJoinSQL() {
		return queryData.getString("JoinSQL");
	}

	protected String getIdSQL() {
		return queryData.getString("IdSQL");
	}

	@Override
	protected void prepareStatementForInsert(PreparedStatement insertionStatement, Product entity) throws DAOException {
		try {
			insertionStatement.setString(1, entity.getName());
			insertionStatement.setBigDecimal(2, entity.getPrice());
		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	protected void prepareStatementForUpdate(PreparedStatement updateStatement, Product newEntity, Product oldEntity)
			throws DAOException {
		try {
			updateStatement.setString(1, newEntity.getName());
			updateStatement.setBigDecimal(2, newEntity.getPrice());
			updateStatement.setBoolean(3, oldEntity.getRemoved());
			updateStatement.setInt(4, oldEntity.getId());
		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	protected void prepareStatementForDelete(PreparedStatement deleteStatement, Product entity) throws DAOException {
		try {
			deleteStatement.setInt(1, entity.getId());
		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}

	@Override
	protected Product parseEntity(ResultSet rs) throws DAOException {
		try {
			return new Product(rs.getInt(1), rs.getString(2), rs.getBigDecimal(3), rs.getBoolean(4));
		} catch (SQLException cause) {
			throw new DAOException(cause);
		}
	}
}