package tp.utn.fieldstypes;

import java.util.ArrayList;
import java.util.List;

public class AbstractFieldsMapping<T> {

	private List<AbstractField> abstracFields = new ArrayList<AbstractField>();
	private Class<T> classType;

	public AbstractFieldsMapping(Class<T> classType, List<AbstractField> abstracFields) {
		this.classType = classType;
		this.abstracFields = abstracFields;
	}

	public List<AbstractField> getAbstracFields() {
		return abstracFields;
	}

	public void setAbstracFields(List<AbstractField> abstracFields) {
		this.abstracFields = abstracFields;
	}

	public Class<T> getClassType() {
		return classType;
	}

	public void setClassType(Class<T> classType) {
		this.classType = classType;
	}
}
