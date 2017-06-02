package client.utn.domain;

import java.sql.Date;

import tp.utn.ann.Column;
import tp.utn.ann.Id;
import tp.utn.ann.Table;

@Table(name="employee",alias="emp")
public class Emp
{
	@Id(strategy=Id.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="hired_date")
	private Date hiredate;

	@Column(name="department_id")
	private Dept dept;

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

	public Date getHiredate()
	{
		return hiredate;
	}

	public void setHiredate(Date hiredate)
	{
		this.hiredate=hiredate;
	}

	public Dept getDept()
	{
		return dept;
	}

	public void setDept(Dept dept)
	{
		this.dept=dept;
	}
	
	@Override
	public boolean equals(Object other)
	{
		Emp o=(Emp)other;
		System.out.println("yo = "+this);
		System.out.println(" o = "+o);
		
		if( id==o.getId() )
		{
			System.out.println("aaaaaaaaaaaaaaaaaaaaaa");
		}
		
		
		return (id==o.getId());//&&o.ename.equals(ename)&&o.hiredate.equals(hiredate);
	}

	@Override
	public String toString()
	{
		return "Emp [empno="+id+", ename="+name+", hiredate="+hiredate+", "+dept+"]";
	}		
}
