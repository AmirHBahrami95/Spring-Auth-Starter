package XX_DOMAIN_NAME.XX_APP_NAME.app.user_rating;

import jakarta.validation.ValidationException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import XX_DOMAIN_NAME.XX_APP_NAME.app.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="usr_rating")
public class UserRating {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne @JoinColumn(referencedColumnName = "uid")
	@NotNull
	private User ratedBy;
	
	@ManyToOne @JoinColumn(referencedColumnName = "uid")
	@NotNull 
	private User subject;
	
	@Min(1) @Max(10)
	private int rating=1;
	
	// TODO @NoXss
	// XXX the following is just a temporary patch
	@Pattern(
		regexp="^[^<>]{3,255}$",
		message="< and > characters are not allowed"
	)
	private String descr=null;
	
	// --------------------------------
	
	public UserRating() {}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getRatedBy() {
		return ratedBy;
	}

	public void setRatedBy(User ratedBy) {
		if(this.subject!=null && ratedBy.getId().equals(subject.getId()))
			throw new ValidationException("A user cannot rate himself");
		this.ratedBy = ratedBy;
	}

	public User getSubject() {
		return subject;
	}

	public void setSubject(User subject) {
		if(this.ratedBy!=null && subject.getId().equals(ratedBy.getId()))
			throw new ValidationException("A user cannot rate himself");
		this.subject = subject;
	}

}
