package client.utn.domain;

import java.util.List;

import tp.utn.ann.Column;
import tp.utn.ann.Gui;
import tp.utn.ann.Id;
import tp.utn.ann.JoinColumn;
import tp.utn.ann.OneToMany;
import tp.utn.ann.Relation;
import tp.utn.ann.Table;

@Table(name="department", alias="d")
public class Dept 
{
	@Id(strategy=Id.ASSIGNED)
	@Column(name="id")
	@Gui(editable=true)
	private Integer id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="loc")
	private String loc;
	
	@OneToMany(type=Emp.class, att="dept")
	@JoinColumn(name="department_id")
	private List<Emp> emps;
	
	public Integer getId()
	{ 
		return id;
	}

	public void setId(Integer id)
	{
		this.id=id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name=name;
	}

	public String getLoc()
	{
		return loc;
	}

	public void setLoc(String loc)
	{
		this.loc=loc;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	public List<Emp> getEmps()
	{
		return emps;
	}

	public void setEmps(List<Emp> emps)
	{
		this.emps = emps;
	}	
	
	public boolean equals(Object o)
	{
		return ((Dept)o).getId()==getId();
				
	}
}
