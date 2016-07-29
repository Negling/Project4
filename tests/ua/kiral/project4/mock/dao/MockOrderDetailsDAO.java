package ua.kiral.project4.mock.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ua.kiral.project4.model.dao.OrderDetailsDAO;
import ua.kiral.project4.model.dao.exceptions.DAOException;
import ua.kiral.project4.model.entity.OrderDetails;

/**
 * Simple OrderDetailsDAO mock realization.
 *
 */
public class MockOrderDetailsDAO implements OrderDetailsDAO {

	@Override
	public boolean create(OrderDetails entity) throws DAOException {
		return entity == null ? false : true;
	}

	@Override
	public boolean update(OrderDetails oldEntity, OrderDetails newEntity) throws DAOException {
		if (oldEntity == null || newEntity == null)
			return false;
		else
			return oldEntity.getOrderId() > 0 ? true : false;
	}

	@Override
	public boolean delete(OrderDetails entity) throws DAOException {
		return entity == null ? false : true;
	}

	@Override
	public List<OrderDetails> getAll() throws DAOException {
		return new ArrayList<>();
	}

	@Override
	public OrderDetails getByOrderId(int orderId) throws DAOException {
		if (orderId < 0)
			return null;
		else
			return new OrderDetails(orderId, new HashMap<>());
	}

	@Override
	public boolean deleteAllByOrderId(int orderId) throws DAOException {
		return orderId < 0 ? false : true;
	}
}