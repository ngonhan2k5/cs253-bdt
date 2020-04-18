package bdt.model;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
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
public class CoronaRecord implements Serializable {
	private static final long serialVersionUID = 1L;
	private String country;
	private String state;
	private String county;
	private LocalDate date;
	private int confirmedCases;
	private int deathCases;
	private int recoveredCases;
	
	public CoronaRecord(String country) {
		this.country = country;
	}
}
