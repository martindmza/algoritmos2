package client.utn.domain;

import tp.utn.ann.Column;
import tp.utn.ann.Id;
import tp.utn.ann.ManyToOne;
import tp.utn.ann.Table;

@Table(name="persona",alias="p")
public class Persona
{
	@Id(strategy=Id.IDENTITY)
	@Column(name="id")
	private Integer id;

	@Column(name="nombre")
	private String nombre;

	@ManyToOne(type=Direccion.class, name="direccion_id")
	public Direccion direccion;

	@ManyToOne(type=Ocupacion.class, name="ocupacion_id")
	public Ocupacion ocupacion;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer idPersona)
	{
		this.id=idPersona;
	}

	public String getNombre()
	{
		return nombre;
	}

	public void setNombre(String nombre)
	{
		this.nombre=nombre;
	}

	public Direccion getDireccion()
	{
		return direccion;
	}

	public void setDireccion(Direccion direccion)
	{
		this.direccion=direccion;
	}

	public Ocupacion getOcupacion()
	{
		return ocupacion;
	}

	public void setOcupacion(Ocupacion ocupacion)
	{
		this.ocupacion=ocupacion;
	}

	@Override
	public String toString()
	{
		return "Persona [idPersona="+ id +", nombre="+nombre+", direccion="+direccion+", ocupacion="+ocupacion+"]";
	}
	
	@Override
	public boolean equals(Object o)
	{
		Persona other = (Persona)o;
		boolean ok = true;
		ok = ok && id==other.getId();
		ok = ok && nombre.equals(other.getNombre());

		if( direccion!=null )
		{
			ok = ok && direccion.getIdDireccion()==other.getDireccion().getIdDireccion();
		}
		else
		{
			ok = ok && other.getDireccion()==null;
		}
		
		if( ocupacion!=null )
		{
			ok = ok && ocupacion.getIdOcupacion()==other.getOcupacion().getIdOcupacion();
		}
		else
		{
			ok = ok && other.getOcupacion()==null;
		}
		
		return ok;
	}
	
	
	

	
}
