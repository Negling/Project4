package ua.kiral.project4.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/**
 * This class maps bucket interface in content page.
 *
 */
public class Bucket implements Serializable {
	private Map<Integer, Integer> bucket;
	private BigDecimal totalCost;

	public Bucket() {
		this.bucket = new HashMap<Integer, Integer>();
		this.totalCost = new BigDecimal("0.00").setScale(2, RoundingMode.HALF_EVEN);
	}

	public Bucket(Map<Integer, Integer> bucket, BigDecimal totalCost) {
		this.bucket = bucket;
		this.totalCost = totalCost.setScale(2, RoundingMode.HALF_EVEN);
	}

	public Map<Integer, Integer> getBucket() {
		return bucket;
	}

	public void setBucket(Map<Integer, Integer> bucket) {
		this.bucket = bucket;
	}

	public BigDecimal getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost.setScale(2, RoundingMode.HALF_EVEN);
	}
	
	/**
	 * Increments value of specified product on one point. If bucket not
	 * contains product - simply returns false value.
	 * 
	 * @param product
	 * @return
	 */
	public boolean increment(Product product) {
		Integer productId = product.getId();

		if (bucket.containsKey(productId)) {

			totalCost = totalCost.add(product.getPrice());
			bucket.replace(productId, bucket.get(productId) + 1);

			return true;
		}

		return false;
	}

	/**
	 * Decrements value of specified product on one point. If amount value of
	 * product is 1 - simply removes product from bucket.
	 * 
	 * @param product
	 * @return
	 */
	public boolean decrement(Product product) {
		Integer productId = product.getId();

		if (bucket.containsKey(productId)) {
			if (bucket.get(productId) <= 1) {

				totalCost = totalCost.subtract(product.getPrice());

				bucket.remove(productId);
				return true;

			} else {

				totalCost = totalCost.subtract(product.getPrice());
				bucket.replace(productId, bucket.get(productId) - 1);

				return true;
			}
		}

		return false;
	}

	public Integer getProductAmount(Product product) {
		return bucket.get(product.getId());
	}

	public Integer putProduct(Product product, Integer amount) {
		for (int i = 0; i < amount; i++) {
			totalCost = totalCost.add(product.getPrice());
		}

		return bucket.put(product.getId(), amount);
	}

	public boolean containsProduct(Product product) {
		return bucket.containsKey(product.getId());
	}

	/**
	 * Removes product from bucket, besides mapping, we need to substract total
	 * price of bucked, which relies to removed product amount.
	 * 
	 * @param product
	 */
	public void removeProduct(Product product) {
		if (bucket.containsKey(product.getId())) {

			int amount = bucket.get(product.getId());

			for (int i = 0; i < amount; i++)
				totalCost = totalCost.subtract(product.getPrice());

			bucket.remove(product.getId());
		}
	}

	public boolean isEmpty() {
		return bucket.isEmpty();
	}

	public void clear() {
		bucket.clear();
		this.totalCost = new BigDecimal("0.00").setScale(2, RoundingMode.HALF_EVEN);
	}

	/**
	 * Returns total products amount counter in map.
	 * 
	 * @return
	 */
	public int size() {
		int size = 0;

		for (Integer productId : bucket.keySet()) {
			size += bucket.get(productId);
		}

		return size;
	}

	public Set<Integer> productsId() {
		return bucket.keySet();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Bucket bucket=").append(bucket).append(", totalCost=").append(totalCost);
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bucket == null) ? 0 : bucket.hashCode());
		result = prime * result + ((totalCost == null) ? 0 : totalCost.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Bucket))
			return false;
		Bucket other = (Bucket) obj;
		if (bucket == null) {
			if (other.bucket != null)
				return false;
		} else if (!bucket.equals(other.bucket))
			return false;
		if (totalCost == null) {
			if (other.totalCost != null)
				return false;
		} else if (!totalCost.equals(other.totalCost))
			return false;
		return true;
	}
}