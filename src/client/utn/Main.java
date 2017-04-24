package client.utn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import tp.utn.FindAll;
import tp.utn.demo.domain.Persona;

public class Main {

	public static void main (String[] args) {

		String usr = "root";
		String pwd = "root";
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/algoritmos";
		
		Connection con = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url,usr,pwd);
			
			for (Persona p : FindAll.findAll(con, Persona.class)) {
				String print =  p.getId() + " - " + p.getNombre();
				System.out.println(print);
			}
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		finally {
			if( con!=null )
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		

		
}
