import java.sql.*;
import java.io.*;
import java.util.Scanner;

public class DBTest {
	public static void main(String[] args) throws SQLException
	{
		Scanner scan = new Scanner(System.in);	
		Connection conn = null;
		
		//연결
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			String dbacct,passwrd,dbname;
			
			//System.out.println("Enter database account:");
			dbacct = "root"; //scan.nextLine();
			//System.out.println("Enter password:");
			passwrd = "j4164038!"; //scan.nextLine();
			//System.out.println("Enter database name:");
			dbname = "company"; //scan.nextLine();
			
			String url = "jdbc:mysql://localhost:3306/"+dbname+"?serverTimezone=UTC";
			conn = DriverManager.getConnection(url, dbacct, passwrd);
			//System.out.println("정상적으로 연결되었습니다.");
		}
		catch(ClassNotFoundException e) {
			System.err.println("드라이버를 로드할 수 없습니다.");
			e.printStackTrace();
		}
		
		System.out.println("<부서별 직원 정보>");
		System.out.println("XXXCOMPANY");
		
		
		//=========================================================
		
		//Number of employee 직원수 구하기
		Statement stmt1 = conn.createStatement( ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    ResultSet rs1 = stmt1.executeQuery("SELECT lname FROM employee");
	    rs1.last();     
	    int rowcount = rs1.getRow();
	    rs1.beforeFirst();
	    System.out.println("Number of employees :"+rowcount);
	    //==========================================================
	    
	    //employee salary 합구하기
	   ResultSet rs2 = stmt1.executeQuery("  SELECT SUM(salary)from employee ");

		while (rs2.next()) {
			String name = rs2.getString(1);
			System.out.println("Total salary:$"+name);
			System.out.println("==========================================================");
		}
        //==========================================================
	
	
		String stmt3 = "select dname, s.fname, count(e.ssn),avg(e.salary),dnumber "
				+ "from employee as e,employee as s,department "
				+ "where  dnumber = e.dno and mgr_ssn = s.ssn group by dname,s.fname order by dnumber";
		
		String stmt2 ="select e.fname,e.lname,e.ssn,e.address,e.sex,e.salary,p.fname,p.lname"
				+ "	from employee as e left outer join employee as p on e.super_ssn=p.ssn"
				+ "	where e.dno=?";
		PreparedStatement p1 = conn.prepareStatement(stmt3);
		p1.clearParameters();
		ResultSet r=p1.executeQuery();
		
		PreparedStatement p = conn.prepareStatement(stmt2);
		p.clearParameters();
		
		while(r.next())
		{
			String dname = r.getString(1);
			String fname = r.getString(2);
			int ssn = r.getInt(3);
			double salary = r.getDouble(4);
			int dnumber_t = r.getInt(5);
			System.out.println("==========================================================");
			System.out.println(dname +" " +  fname + " "+ ssn + " "+ salary + " ");
			
			p.setInt(1, dnumber_t);
			ResultSet r1=p.executeQuery();
			System.out.println("----------------------------------------------------------");
			while(r1.next())
			{
				
				String fname1 = r1.getString(1);
				String lname = r1.getString(2);
				String ssn2 = r1.getString(3);
				String address = r1.getString(4);
				String sex = r1.getString(5);
				double salary1 = r1.getDouble(6);
				String s_fname = r1.getString(7);
				String s_lname = r1.getString(8);
				
				System.out.println(fname1 +" " + lname + " "+ ssn2 +" " + address + " " + sex +" " + salary1+ " "+ s_fname +" "+ s_lname);
			}
		}
		System.out.println("==========================================================");
		
		try {
			if(conn!=null)
			{

				conn.close();
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
}
