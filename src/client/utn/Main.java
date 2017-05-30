package client.utn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import client.utn.domain.Dept;
import client.utn.domain.Persona;
import tp.utn.methods.Find;
import tp.utn.methods.FindAll;
import tp.utn.methods.Utn;

public class Main {

	public static void main(String[] args) throws Exception {

		String usr = "root";
		String pwd = "root";
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/algoritmos";

		Connection con = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, usr, pwd);

//			findAll ---
//			for (Persona p : FindAll.findAll(con, Persona.class)) {
//				System.out.println(p);
//			}
			
			for (Dept dept : FindAll.findAll(con, Dept.class)) {
				System.out.println(dept);
			}
			
			System.exit(1);
			
			//find ---
			Persona p2 = Find.find(con, Persona.class, 2);
			System.out.println(p2);
			
			//query ---
			for (Persona p : Utn.query(con, Persona.class, "nombre LIKE '%a%'", args)) {
				System.out.println(p + " encontrado con query");
			}

			
			
//			//insert ---
//			Direccion nueva = new Direccion("Rivadavia", 700);
//			int direccionId = Insert.insert(con, nueva);
//			System.out.println("Objeto creado : " + nueva);
//			
//			//delete por id---
//			int direccionBorrada = Delete.delete(con, Direccion.class, direccionId);
//			if (direccionBorrada == 0) {
//				System.out.println("Error: Objeto " + nueva + " no eliminado!!");
//			} else {
//				System.out.println("Objeto " + nueva + " eliminado");
//			}
//
//			//deletes por xql---
//			Insert.insert(con, new Direccion("Juan b Justo", 700));
//			Insert.insert(con, new Direccion("Juan b Justo", 1000));
//			Insert.insert(con, new Direccion("Juan b Justo", 1004));
//			
//			int direccionBorrada2 = Delete.delete(con, Direccion.class, "calle=?", "Juan b Justo");
//			if (direccionBorrada == 0) {
//				System.out.println("Error: Objetos " + nueva + " no eliminados!!");
//			} else {
//				System.out.println(direccionBorrada2 + " Objetos con xql eliminados");
//			}
			
			
			

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
		}
		System.out.println("");
		System.out.println("have a nice day :)");
	}
}
