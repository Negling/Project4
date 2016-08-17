package ua.kiral.project4.mock.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ua.kiral.project4.model.dao.ProductDAO;
import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.Order;
import ua.kiral.project4.model.entity.Product;

/**
 * Simple realization of ProductDAO.
 *
 */
public class MockProductDAO implements ProductDAO {

	@Override
	public boolean create(Product entity) throws DAOException {
		return entity == null ? false : true;
	}

	@Override
	public boolean update(Product oldEntity, Product newEntity) throws DAOException {
		if (oldEntity == null || newEntity == null)
			return false;
		else
			return oldEntity.getId() > 0 ? true : false;
	}

	@Override
	public boolean delete(Product entity) throws DAOException {
		return entity == null ? false : true;
	}

	@Override
	public List<Product> getAll() throws DAOException {
		return new ArrayList<>();
	}

	@Override
	public Product getProductByID(int productId) throws DAOException {
		if (productId < 0)
			return new Product(-1, "mock", new BigDecimal(0.00), false);
		else if (productId == 0)
			return null;
		else
			return new Product(1, "mock", new BigDecimal(0.00), false);
	}

	@Override
	public int getProductId(Product product) throws DAOException {
		if (product == null)
			return 0;
		else if (product.getId() == null)
			return -1;
		else
			return product.getId();
	}

	@Override
	public List<Product> getAllByName(String name) throws DAOException {
		return name == null ? null : new ArrayList<>();
	}

	@Override
	public List<Product> getAllByStatus(Boolean status) throws DAOException {
		return status == null ? null : new ArrayList<>();
	}

	@Override
	public List<Product> getLower(BigDecimal price) throws DAOException {
		return price == null ? null : new ArrayList<>();
	}

	@Override
	public List<Product> getLowerAndEquals(BigDecimal price) throws DAOException {
		return price == null ? null : new ArrayList<>();
	}

	@Override
	public List<Product> getGreater(BigDecimal price) throws DAOException {
		return price == null ? null : new ArrayList<>();
	}

	@Override
	public List<Product> getGreaterAndEquals(BigDecimal price) throws DAOException {
		return price == null ? null : new ArrayList<>();
	}

	@Override
	public boolean deleteById(int productID) throws DAOException {
		return productID < 0 ? false : true;
	}

	@Override
	public boolean deleteAllByName(String name) throws DAOException {
		return name == null ? false : true;
	}

	@Override
	public boolean deleteAllByPrice(BigDecimal price) throws DAOException {
		return price == null ? false : true;
	}

	@Override
	public List<Product> getProductsFromOrder(Order order) throws DAOException {
		return order == null ? null : new ArrayList<>();
	}
}