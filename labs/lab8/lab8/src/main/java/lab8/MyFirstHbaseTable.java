package lab8;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
import org.apache.hadoop.hbase.util.Bytes;

public class MyFirstHbaseTable {

	public static void main(String... args) throws IOException {

		Configuration config = HBaseConfiguration.create();
		try (Connection conn = ConnectionFactory.createConnection(config); Admin admin = conn.getAdmin()) {
			String action = "create";

			if (args.length > 0)
				action = args[0];


			switch (action) {
				case "update":
					updateData(conn);
					break;
				case "count":
					countData(conn);
					break;
				default:
					createTable(conn);
					putData(conn);
			}
		}

	}

	private static void countData(Connection conn) throws IOException {
		System.out.println("Count by scan"+ Util.count(conn));
	}

	private static void createTable(Connection conn) throws IOException {
		try (Admin admin = conn.getAdmin()) {
			HTableDescriptor table = new HTableDescriptor(TableName.valueOf(Util.TABLE_NAME));
			table.addFamily(new HColumnDescriptor(Util.CF_PERS_DATA).setCompressionType(Algorithm.NONE));
			table.addFamily(new HColumnDescriptor(Util.CF_PROF_DATA));

			System.out.print("Creating table.... ");

			if (admin.tableExists(table.getTableName())) {
				admin.disableTable(table.getTableName());
				admin.deleteTable(table.getTableName());
			}
			admin.createTable(table);

			System.out.println("Create table Done!");

			System.out.println(" Done!");
		}
	}

	private static void putData(Connection conn) throws IllegalArgumentException, IOException {

		Map<String, Emp> map = new HashMap<>();
		map.put("1", Emp.a("John", "Boston", "Manager", 150000));
		map.put("2", Emp.a("Mary", "New York", "Sr. Engineer", 130000));
		map.put("3", Emp.a("Bob", "Fremont", "Jr. Engineer", 90000));

		Util.putAll(conn, map);
		System.out.println("data inserted");

	}

	private static void updateData(Connection conn) throws IllegalArgumentException, IOException {
		String key = "3";
		Emp emp = Util.get(conn, key);
		if (emp != null) {
			emp.setDesignation("Sr. Engineer");
			emp.setSalary(emp.getSalary() * 1.05);

			Util.put(conn, key, emp);
		}

		System.out.println("data updated");

	}

}

class Util {
	public static final byte[] TABLE_NAME = Bytes.toBytes("User");
	public static final byte[] CF_PERS_DATA = Bytes.toBytes("Personal Data");
	public static final byte[] CF_PROF_DATA = Bytes.toBytes("Professional Data");

	public static Put getPut(String key, Emp e) {
		Put p = new Put(Bytes.toBytes(key));
		p.addColumn(CF_PERS_DATA, tb("Name"), tb(e.getName()));
		p.addColumn(CF_PERS_DATA, tb("City"), tb(e.getName()));
		p.addColumn(CF_PROF_DATA, tb("Designation"), tb(e.getDesignation()));
		p.addColumn(CF_PROF_DATA, tb("salary"), tb(e.getSalary()));
		return p;
	}

	public static void putAll(Connection conn, Map<String, Emp> map) throws IllegalArgumentException, IOException {
		try(
			Table hTable = conn.getTable(TableName.valueOf(Util.TABLE_NAME));
		){

			for (String key : map.keySet()) {
				hTable.put(Util.getPut(key, map.get(key)) );
			}
			
		}
	}

	public static void put(Connection conn, String key, Emp e) throws IllegalArgumentException, IOException {
		try (Table hTable = conn.getTable(TableName.valueOf(Util.TABLE_NAME));) {

			hTable.put(Util.getPut(key, e));

		}
	}

	public static Emp get(Connection conn, String key) throws IllegalArgumentException, IOException {
		try (Table hTable = conn.getTable(TableName.valueOf(Util.TABLE_NAME));) {

			Result re = hTable.get(new Get(Bytes.toBytes(key)));

			return empFromResult(re);

		}
	}
	private static Emp empFromResult(Result re){
		if (re.isEmpty()) {
			return null;
		}
		return Emp.a(
			Bytes.toString(re.getValue(CF_PERS_DATA, tb("Name"))),
			Bytes.toString(re.getValue(CF_PERS_DATA, tb("City"))),
			Bytes.toString(re.getValue(CF_PROF_DATA, tb("Designation"))),
			Bytes.toDouble(re.getValue(CF_PROF_DATA, tb("salary")))
		);
	}

	public static int count(Connection conn) throws IOException {
		Scan scan = new Scan();

		// Scanning the required columns
		scan.addColumn(CF_PERS_DATA, Bytes.toBytes("Name"));
		scan.addColumn(CF_PERS_DATA, Bytes.toBytes("City"));
		scan.addColumn(CF_PROF_DATA, Bytes.toBytes("Designation"));
		scan.addColumn(CF_PROF_DATA, Bytes.toBytes("salary"));

		int ret = 0;
		try (Table hTable = conn.getTable(TableName.valueOf(Util.TABLE_NAME));
				ResultScanner scanner = hTable.getScanner(scan);) {

			// Reading values from scan result
			for (Result result = scanner.next(); result != null; result = scanner.next()){
				ret++;
				System.out.println("Found row : " + empFromResult(result));
			}
			return ret;
		}
	}

	public static byte[] tb(String s) {
		return Bytes.toBytes(s);
	}

	public static byte[] tb(double s) {
		return Bytes.toBytes(s);
	}
}

class Emp {
	private int empId;
	private String name, city, designation;
	private double salary;

	public static Emp a(String name, String city, String designation, double salary) {
		Emp e = new Emp();
		e.setName(name);
		e.setCity(city);
		e.setDesignation(designation);
		e.setSalary(salary);
		return e;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		
		return String.format("Name: %s, city: %s, designation: %s, salary %f", name, city, designation, salary);
	}
}
