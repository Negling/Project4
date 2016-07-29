package ua.kiral.project4.mock.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ua.kiral.project4.model.dao.OrderDAO;
import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.Order;

/**
 * Simple mock realization of OrderDAO.
 *
 */
public class MockOrderDAO implements OrderDAO {

	@Override
	public boolean create(Order entity) throws DAOException {
		if (entity == null)
			return false;

		return entity.getOwnerId() > 0 ? true : false;
	}

	@Override
	public boolean update(Order oldEntity, Order newEntity) throws DAOException {
		if (oldEntity == null || newEntity == null)
			return false;
		else
			return oldEntity.getOrderId() > 0 ? true : false;
	}

	@Override
	public boolean delete(Order entity) throws DAOException {
		if (entity == null)
			return false;
		else
			return entity.getOrderId() > 0 ? true : false;
	}

	@Override
	public List<Order> getAll() throws DAOException {
		return new ArrayList<>();
	}

	@Override
	public Order getOrderByID(Integer orderId) throws DAOException {
		if (orderId == null)
			return null;
		else if (orderId > 0)
			return new Order(1, -1, new Date(), "mock", new BigDecimal("0.00"));
		else
			return new Order(-1, -1, new Date(), "mock", new BigDecimal("0.00"));
	}

	@Override
	public int getOrderId(Order order) throws DAOException {
		if (order == null)
			return -1;
		if (order.getOwnerId() == 0)
			return 0;

		return order.getOwnerId() > 0 ? 1 : -1;
	}

	@Override
	public List<Order> getAllByStatus(String status) throws DAOException {
		return status == null ? null : new ArrayList<>();
	}

	@Override
	public List<Order> getAllByOwner(Integer ownerId) throws DAOException {
		if (ownerId == null)
			return null;
		if (ownerId < 0)
			return new ArrayList<>();
		else {
			List<Order> list = new ArrayList<>();
			list.add(new Order(1, 1, new Date(), "mock", new BigDecimal("0.00")));
			return list;
		}
	}

	@Override
	public List<Order> getAllByDate(Date date) throws DAOException {
		return date == null ? null : new ArrayList<>();
	}

	@Override
	public boolean deleteAllByStatus(String status) throws DAOException {
		return status == null ? false : true;
	}

	@Override
	public boolean deleteAllByOwner(Integer owner) throws DAOException {
		return owner == null ? false : true;
	}

	@Override
	public boolean deleteAllByDate(Date date) throws DAOException {
		return date == null ? false : true;
	}
}