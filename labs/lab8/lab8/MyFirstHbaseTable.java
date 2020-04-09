package lab8;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
import org.apache.hadoop.hbase.util.Bytes;

public class MyFirstHbaseTable
{

	

	public static void main(String... args) throws IOException
	{

		Configuration config = HBaseConfiguration.create();

		try (Connection connection = ConnectionFactory.createConnection(config);
				Admin admin = connection.getAdmin())
		{
			HTableDescriptor table = new HTableDescriptor(TableName.valueOf(Util.TABLE_NAME));
			table.addFamily(new HColumnDescriptor(Util.CF_PERS_DATA).setCompressionType(Algorithm.NONE));
			table.addFamily(new HColumnDescriptor(Util.CF_PROF_DATA));

			System.out.print("Creating table.... ");

			if (admin.tableExists(table.getTableName()))
			{
				admin.disableTable(table.getTableName());
				admin.deleteTable(table.getTableName());
			}
			admin.createTable(table);

			System.out.println("Create table Done!");
			// putAll
			putAll(connection);

			System.out.println("Create data Done!");

			System.out.println(" Done!");
		}
	}

	private static void putAll(Connection connection) throws IllegalArgumentException, IOException {
		try(
			Table hTable = connection.getTable(TableName.valueOf(Util.TABLE_NAME));
		){

			hTable.put(Util.getPut("1", Emp.a("John", "Boston", "Manager", 150000)));
			hTable.put(Util.getPut("2", Emp.a("Mary", "New York", "Sr. Engineer", 130000)));
			hTable.put(Util.getPut("3", Emp.a("Bob", "Fremont", "Jr. Engineer", 90000)));

			System.out.println("data inserted");

			
		}
	}

	
}

class Util{
	public static final byte[] TABLE_NAME = Bytes.toBytes("User");
	public static final byte[] CF_PERS_DATA = Bytes.toBytes("Personal Data");
	public static final byte[] CF_PROF_DATA = Bytes.toBytes("Professional Data");

	public static Put getPut(String key, Emp e){
		Put p = new Put(Bytes.toBytes(key) );
		p.addColumn(CF_PERS_DATA, tb("Name"), tb(e.getName()));
		p.addColumn(CF_PERS_DATA, tb("City"), tb(e.getName()));
		p.addColumn(CF_PROF_DATA, tb("Designation"), tb(e.getDesignation()));
		p.addColumn(CF_PROF_DATA, tb("salary"), tb(e.getSalary()));
		return p;
	}

	public static byte[] tb(String s){
		return Bytes.toBytes(s);
	}
	public static byte[] tb(double s){
		return Bytes.toBytes(s);
	}
}

class Emp {
	private int empId;
	private String name, city, designation;
	private double salary;

	public static Emp a(String name, String city, String designation, double salary){
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
}
