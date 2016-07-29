package ua.kiral.project4.model.entity;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Simple container of order details data.
 *
 */
public class OrderDetails implements Serializable {
	private Integer orderId;
	private Map<Integer, Integer> products;
	
	public OrderDetails() {
	}

	public OrderDetails(Integer orderId, Map<Integer, Integer> products) {
		this.orderId = orderId;
		this.products = products;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Map<Integer, Integer> getProducts() {
		return products;
	}

	public void setProducts(Map<Integer, Integer> products) {
		this.products = products;
	}

	public boolean isEmpty() {
		return products.isEmpty();
	}

	public boolean containsProductId(Object productId) {
		return products.containsKey(productId);
	}

	public Integer getProductAmount(Object productId) {
		return products.get(productId);
	}

	public Integer putProduct(Integer productId, Integer amount) {
		return products.put(productId, amount);
	}

	public Integer removeProduct(Object productId) {
		return products.remove(productId);
	}

	public Set<Integer> productsId() {
		return products.keySet();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OrderDetails orderId=").append(orderId).append(", products(productId, amount)=")
				.append(products);
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
		result = prime * result + ((products == null) ? 0 : products.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof OrderDetails))
			return false;
		OrderDetails other = (OrderDetails) obj;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
			return false;
		if (products == null) {
			if (other.products != null)
				return false;
		} else if (!products.equals(other.products))
			return false;
		return true;
	}
	
}