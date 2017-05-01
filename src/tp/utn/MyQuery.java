package tp.utn;

import java.util.ArrayList;
import java.util.List;
import java.time.Instant;
import java.sql.Timestamp;
import tp.utn.ann.Table;

public class MyQuery {

	private Table table;
	private List<String> fields;
	private List<String> joins;
	
	public MyQuery (Table table) {
		this.table = table;
		
		this.fields = new ArrayList<String>();
		this.joins = new ArrayList<String>();
	}
	
	public List<String> getFields() {
		return fields;
	}
	
	public List<String> getJoins() {
		return joins;
	}
	
	public void addJoinTable(Table joinedTable, String thisTableField, String JoinedTableField) {
		String alias = joinedTable.alias();
		if (alias.trim().equals("")) {
			alias = joinedTable.name();
		}
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Instant instant = timestamp.toInstant();
		alias += "_" + instant.toEpochMilli();
		
		String joinString = "JOIN " + joinedTable.name() + " AS " + alias 
								+ " ON " + this.table;
	}
	
	public void addField(List<String> fields) {
		this.fields = fields;
	}
}
