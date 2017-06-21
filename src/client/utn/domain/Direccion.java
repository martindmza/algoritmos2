package client.utn.domain;

import java.util.Collection;

import tp.utn.ann.Column;
import tp.utn.ann.Id;
import tp.utn.ann.OneToMany;
import tp.utn.ann.Relation;
import tp.utn.ann.Table;
@Table(name="direccion", alias="d")
public class Direccion
{
	@Id(strategy=Id.IDENTITY)
	@Column(name="id")
	private Integer idDireccion;

	@Column(name="calle")
	private String calle;

	@Column(name="numero")
	private int numero;
	
	@OneToMany(type=Persona.class,att="direccion", fetchType=OneToMany.LAZY)
	private Collection<Persona> personas;
	
	public Direccion () {}
	
	public Direccion(String calle, int numero) {
		this.calle = calle;
		this.numero = numero;
	}

	public Collection<Persona> getPersonas()
	{
		return personas;
	}

	public void setPersonas(Collection<Persona> personas)
	{
		this.personas=personas;
	}

	public Integer getIdDireccion()
	{
		return idDireccion;
	}

	public void setIdDireccion(Integer idDireccion)
	{
		this.idDireccion=idDireccion;
	}

	public String getCalle()
	{
		return calle;
	}

	public void setCalle(String calle)
	{
		this.calle=calle;
	}

	public int getNumero()
	{
		return numero;
	}

	public void setNumero(int numero)
	{
		this.numero=numero;
	}

	@Override
	public String toString()
	{
		return getCalle()+" "+getNumero();
	}

	@Override
	public boolean equals(Object obj)
	{
		Direccion o=(Direccion)obj;
		boolean ok = idDireccion==o.getIdDireccion()&& numero==o.getNumero();
		String sCalle=calle!=null?calle:"null";
		String sOCalle=o.getCalle()!=null?o.getCalle():"null";
		return ok&&sCalle.equals(sOCalle);
	}






}
