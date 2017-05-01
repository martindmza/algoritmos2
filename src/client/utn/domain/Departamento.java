package client.utn.domain;

import java.util.List;

import tp.utn.ann.Column;
import tp.utn.ann.Gui;
import tp.utn.ann.Id;
import tp.utn.ann.OneToMany;
import tp.utn.ann.Relation;
import tp.utn.ann.Table;

@Table(name = "departamentos", alias = "d")
public class Departamento {
	@Id(strategy = Id.ASSIGNED)
	@Column(name = "id")
	// @Gui(editable=true)
	private Integer id;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "localidad")
	private String localidad;

	@OneToMany(type = Persona.class, att = "departamento")
	private List<Persona> personas;

	public Integer getId() {
		return id;
	}

	public void setDeptno(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	@Override
	public String toString() {
		return getNombre();
	}

	public List<Persona> getPersonas() {
		return personas;
	}

	public void setPersonas(List<Persona> emps) {
		this.personas = emps;
	}

	public boolean equals(Object o) {
		return ((Departamento) o).getId() == getId();
	}
}
