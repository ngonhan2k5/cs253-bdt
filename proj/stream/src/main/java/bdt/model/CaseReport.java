package bdt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class CaseReport {
	private long confirmedCases;
	private long recoveredCases;
	private long deathCases;

	@Override
	public String toString() {
		return new StringBuilder()
				.append(confirmedCases)
				.append(",")
				.append(recoveredCases)
				.append(",")
				.append(deathCases)
				.toString();
	}
}
