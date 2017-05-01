package client.utn.domain;

import java.sql.Date;

import tp.utn.ann.Column;
import tp.utn.ann.Id;
import tp.utn.ann.Table;

@Table(name = "empleados")
public class Empleado {
	@Id(strategy = Id.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "ingreso")
	private Date ingreso;

	// @Column(name="deptno")
	private Departamento departamento;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getIngreso() {
		return ingreso;
	}

	public void setIngreso(Date ingreso) {
		this.ingreso = ingreso;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	@Override
	public boolean equals(Object other) {
		Empleado o = (Empleado) other;
		System.out.println("yo = " + this);
		System.out.println(" o = " + o);

		if (id == o.getId()) {
			System.out.println("aaaaaaaaaaaaaaaaaaaaaa");
		}

		return (id == o.getId());// &&o.ename.equals(ename)&&o.hiredate.equals(hiredate);
	}

	@Override
	public String toString() {
		return "Emp [empno=" + id + ", ename=" + nombre + ", hiredate=" + ingreso + ", " + departamento + "]";
	}
}
