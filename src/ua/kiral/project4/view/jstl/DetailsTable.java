package ua.kiral.project4.view.jstl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.kiral.project4.model.entity.OrderDetails;
import ua.kiral.project4.model.entity.Product;

/**
 * Custom 'details' tag handler. Displays information about order details as
 * part of table html tag(concludes every segment into tr and td tags), or as
 * simple strings, with '|' delimeter between segments.
 * 
 * Contains three required attributes: 1) List of products, to recieve
 * information about current product in order(name, price). 2) Order details,
 * with info about amount of purchased product. 3) Display model trigger, if
 * true - print as part of table, othervise as string with delimeters. To create
 * result string we use StringBuilder instance.
 */
public class DetailsTable extends TagSupport {
	final static Logger logger = LogManager.getLogger(DetailsTable.class);
	private boolean asTable;
	private List<Product> products;
	private OrderDetails details;

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public void setDetails(OrderDetails details) {
		this.details = details;
	}

	public void setAsTable(boolean asTable) {
		this.asTable = asTable;
	}

	@Override
	public int doStartTag() throws JspException {
		if (details != null) {
			try {
				StringBuilder builder = new StringBuilder();

				if (asTable)
					formatAsTable(builder);
				else
					formatRegular(builder);

				pageContext.getOut().print(builder.toString());
			} catch (Exception ex) {
				logger.error("Unexpected behavior: Tags, DetailsTable:", ex);
			}
		}
		return SKIP_BODY;
	}

	/**
	 * Formates data as part of html 'table' tag.
	 * @param builder
	 */
	private void formatAsTable(StringBuilder builder) {
		for (Product product : products) {
			Integer amount = details.getProductAmount(product.getId());
			BigDecimal totalCost = calculateTotalCost(product.getPrice(), amount);

			builder.append("<tr>");
			toColumn(builder, product.getName());
			toColumn(builder, amount.toString());
			toColumn(builder, totalCost.toString()).append("</tr>");
		}
	}

	/**
	 * Concludes element into td /td tags.
	 * 
	 * @param builder
	 * @param element
	 * @return
	 */
	private StringBuilder toColumn(StringBuilder builder, String element) {
		return builder.append("<td>").append(element).append("</td>");
	}

	/**
	 * Formates result as string with '|' delimeter between elements.
	 * @param builder
	 */
	private void formatRegular(StringBuilder builder) {
		for (Product product : products) {
			int amount = details.getProductAmount(product.getId());
			BigDecimal totalCost = calculateTotalCost(product.getPrice(), amount);

			builder.append(product.getName()).append(" | ").append(amount).append(" | ").append(totalCost)
					.append("</br>");
		}
	}
	
	/**
	 * Returns BigDecimal value of total product price in this order, short form
	 * - amount * price.
	 * 
	 * @param price
	 * @param amount
	 * @return
	 */
	private BigDecimal calculateTotalCost(BigDecimal price, int amount) {
		BigDecimal result = new BigDecimal("0.00").setScale(2, RoundingMode.HALF_EVEN);

		for (int i = 0; i < amount; i++) {
			result = result.add(price);
		}

		return result;
	}
}