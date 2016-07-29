package ua.kiral.project4.model.dao;

import java.util.List;

import ua.kiral.project4.model.dao.exceptions.DAOException;

/**
 * Unified interface to unite CRUD operations.
 *
 * @param <E>
 */
public interface GenericDAO<E> {

	/**
	 * Creates new record wich complies to current entity.
	 * 
	 * @param entity
	 * @return <code>true</code> in case of successm false otherwise
	 * @throws DAOException
	 */
	public abstract boolean create(E entity) throws DAOException;

	/**
	 * Updates existing record which complies to current entity.
	 * 
	 * @param oldEntity
	 * @param newEntity
	 * @return <code>true</code> in case of successm false otherwise
	 * @throws DAOException
	 */
	public abstract boolean update(E oldEntity, E newEntity) throws DAOException;

	/**
	 * Deletes existing record which complies to current entity.
	 * 
	 * @param entity
	 * @return <code>true</code> in case of successm false otherwise
	 * @throws DAOException
	 */
	public abstract boolean delete(E entity) throws DAOException;

	/**
	 * Returns all entity records as List.
	 * 
	 * @return
	 * @throws DAOException
	 */
	public abstract List<E> getAll() throws DAOException;
}