package bdt.model;

import java.io.Serializable;

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
public class HBCoronaRecord implements Serializable {
	private static final long serialVersionUID = 1L;
	private String country;
	private String state;
	private String county;
	private String date;
	private int confirmedCases;
	private int deathCases;
	private int recoveredCases;
}
