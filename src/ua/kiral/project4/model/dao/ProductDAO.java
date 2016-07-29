package ua.kiral.project4.model.dao;

import java.math.BigDecimal;
import java.util.List;

import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.Order;
import ua.kiral.project4.model.entity.Product;

/**
 * Specifies operations wich relate to Product entity.
 *
 */
public interface ProductDAO extends GenericDAO<Product> {
	
	/**
	 * Returns product id wich mathes to specified product;
	 * 
	 * @param Id
	 * @return -1 if there was no such product.
	 * @throws DAOException
	 */
	int getProductId(Product product) throws DAOException;

	/**
	 * Returns Product wich mathes to specified product ID;
	 * 
	 * @param productId
	 * @return null if there was no such product.
	 * @throws DAOException
	 */
	public Product getProductByID(int productId) throws DAOException;

	/**
	 * Returns list of Products wich mathes to specified product name;
	 * 
	 * @param name
	 * @return null if no products match to argument.
	 * @throws DAOException
	 */
	public List<Product> getAllByName(String name) throws DAOException;

	/**
	 * Returns list of Products wich price lower than specified price value;
	 * 
	 * @param price
	 * @return null if no products match to argument.
	 * @throws DAOException
	 */
	public List<Product> getLower(BigDecimal price) throws DAOException;

	/**
	 * Returns list of Products wich price lower and equals to specified price
	 * value;
	 * 
	 * @param price
	 * @return null if no products match to argument.
	 * @throws DAOException
	 */
	public List<Product> getLowerAndEquals(BigDecimal price) throws DAOException;

	/**
	 * Returns list of Products wich price greater than specified price value;
	 * 
	 * @param price
	 * @return null if no products match to argument.
	 * @throws DAOException
	 */
	public List<Product> getGreater(BigDecimal price) throws DAOException;

	/**
	 * Returns list of Products wich price greated and equals to specified price
	 * value.
	 * 
	 * @param price
	 * @return null if no products match to argument.
	 * @throws DAOException
	 */
	public List<Product> getGreaterAndEquals(BigDecimal price) throws DAOException;
	
	/**
	 * Returns list of Products wich figures in specifien order.
	 * 
	 * @param price
	 * @return null if no products match to argument.
	 * @throws DAOException
	 */
	public List<Product> getProductsFromOrder(Order order) throws DAOException;
	
	/**
	 * Deletes product wich mathes to specified product is;
	 * 
	 * @param id
	 * @return <code>true</code> in case of success, false otherwise
	 * @throws DAOException
	 */
	public boolean deleteById(int productID) throws DAOException;

	/**
	 * Deletes all Products wich mathes to specified product name;
	 * 
	 * @param name
	 * @return <code>true</code> in case of success, false otherwise
	 * @throws DAOException
	 */
	public boolean deleteAllByName(String name) throws DAOException;

	/**
	 * Deletes all Products wich mathes to specified order owner;
	 * 
	 * @param price
	 * @return <code>true</code> in case of success, false otherwise
	 * @throws DAOException
	 */
	public boolean deleteAllByPrice(BigDecimal price) throws DAOException;
}