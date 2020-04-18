package bdt.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Corona Record
 * 
 * @author khanhnguyen
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CaseReportByCountryDate extends CaseReport implements Serializable {
	private static final long serialVersionUID = 1L;
	private String country;
	private String date;

	@Override
	public String toString() {
		return new StringBuilder()
				.append(country)
				.append(",")
				.append(date)
				.append(",")
				.append(super.toString())
				.toString();
	}

}
