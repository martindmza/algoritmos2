package client.utn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import client.utn.domain.Dept;
import client.utn.domain.Direccion;
import client.utn.domain.Emp;
import client.utn.domain.Ocupacion;
import client.utn.domain.Persona;
import client.utn.domain.TipoOcupacion;
import tp.utn.methods.Delete;
import tp.utn.methods.Find;
import tp.utn.methods.FindAll;
import tp.utn.methods.Insert;
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
			
			boolean findall = false;
			boolean find = false;
			boolean query = false;
			boolean insert = true;
			boolean delete = true;

			if (findall) {
				System.out.println("Personas:");
				for (Persona p : FindAll.findAll(con, Persona.class)) {
					System.out.println(p);
				}
				
				System.out.println("Departamentos:");
				for (Dept dept : FindAll.findAll(con, Dept.class)) {
					System.out.println(dept);
				}
				
				System.out.println("Empleados:");
				for (Emp emp : FindAll.findAll(con, Emp.class)) {
					System.out.println(emp);
				}
			}

			
			if (find) {
				Persona p2 = Find.find(con, Persona.class, 2);
				System.out.println(p2);
				
				Persona p4 = Find.find(con, Persona.class, 4);
				System.out.println(p4);
			}
			
			if (query) {
				for(Persona p : Utn.query(con, Persona.class, "$ocupacion.tipoOcupacion.descripcion = ?", "Empleado")) {
					System.out.println(p);
				}
				
				for(Persona p : Utn.query(con, Persona.class, "$direccion.calle = ?", "Tapalque")) {
					System.out.println(p);
				}
				
				for(Persona p : Utn.query(con, Persona.class, "$nombre = ?", "Tincho")) {
					System.out.println(p);
				}
				
				for(Persona p : Utn.query(	con,
											Persona.class,
											"$ocupacion.tipoOcupacion.descripcion = ? AND $ocupacion.descripcion = ?",
											"Empleado", "Desarrollador")) {
					System.out.println(p);
				}
			}

			Persona p1 = new Persona("Bob");
			if (insert) {
				Direccion d1 = new Direccion("Rivadavia", 700);
				Insert.insert(con, d1);
				System.out.println("Objeto creado : " + d1);
				
				TipoOcupacion to1 = new TipoOcupacion("Docente");
				Ocupacion o1 = new Ocupacion("Profesor", to1);
				
				p1.setDireccion(d1);
				p1.setOcupacion(o1);

				Insert.insert(con, p1);
				System.out.println("Objeto creado : " + p1);
			}
			

			if (delete) {
				int resultado = Delete.delete(con, Persona.class, p1.getId());
				if (resultado == 1) {
					System.out.println("Objeto Eliminado : " + p1);
				} else {
					System.out.println("Objeto No Eliminado : " + p1);
				}
			}
			
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
