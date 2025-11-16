package XX_DOMAIN_NAME.XX_APP_NAME.app.utils;

public class Range<T> {
	
	private T lowerRange;
	private T upperRange;
	
	public Range() {}

	public T getLowerRange() {
		return lowerRange;
	}

	public void setLowerRange(T lowerRange) {
		this.lowerRange = lowerRange;
	}

	public T getUpperRange() {
		return upperRange;
	}

	public void setUpperRange(T upperRange) {
		this.upperRange = upperRange;
	}
	
}
