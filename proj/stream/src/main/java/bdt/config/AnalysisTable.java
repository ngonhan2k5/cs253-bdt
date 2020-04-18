package bdt.config;

public enum AnalysisTable {

	CASES_BY_COUNTRY("cases_by_country"), 
	CASES_BY_DATE("cases_by_date"),
	CASES_BY_COUNTRY_DATE("cases_by_country_date"),
	PILOT("pilot");

	private String value;

	AnalysisTable(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

}
