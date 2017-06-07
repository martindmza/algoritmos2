package tp.utn.fieldstypes;

public class ObjectMappedRow<T> {

	T entity;
	AbstractFieldsMapping abstractField;

	public ObjectMappedRow(T entity, AbstractFieldsMapping abstractField) {
		this.entity = entity;
		this.abstractField = abstractField;
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public AbstractFieldsMapping getAbstractField() {
		return abstractField;
	}

	public void setAbstractField(AbstractFieldsMapping abstractField) {
		this.abstractField = abstractField;
	}
	
	
}
