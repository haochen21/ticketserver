package ticket.server.model.security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import ticket.server.model.Constants;
import ticket.server.model.store.Product;

@Entity
@Table(name = "OPENRANGE", indexes = { @Index(name = "IDX_OPENRANGE_MERCHANT", columnList = "MERCHANT_ID") })
public class OpenRange implements Serializable {

	@Id
	@GeneratedValue(generator = Constants.ID_GENERATOR)
	protected Long id;

	@NotNull
	@Temporal(TemporalType.TIME)
	@Column(nullable = false)
	protected Date beginTime;

	@NotNull
	@Temporal(TemporalType.TIME)
	@Column(nullable = false)
	protected Date endTime;

	@Enumerated(EnumType.STRING)
	@Column(name = "TYPE", nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
	protected OpenRangeType type;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MERCHANT_ID", nullable = false)
	@JsonBackReference
	protected Merchant merchant;

	@ManyToMany(mappedBy = "openRanges")
	protected Set<Product> products = new HashSet<Product>();

	private static final long serialVersionUID = -4343731302412808649L;

	public OpenRange() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public OpenRangeType getType() {
		return type;
	}

	public void setType(OpenRangeType type) {
		this.type = type;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OpenRange other = (OpenRange) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OpenRange [beginTime=" + beginTime + ", endTime=" + endTime + "]";
	}

}
