package tp.utn.fieldstypes;

import java.util.ArrayList;
import java.util.List;

public class AbstractFieldsMapping<T, C> {

	private List<PrimitiveField> primitiveFields = new ArrayList<PrimitiveField>();
	private Class<T> classType;
	private Class<C> containerClass;
	private String containerAlias;
	protected AbstractField containerClassField;

	public AbstractFieldsMapping(Class<T> classType, Class<C> containerClass, List<PrimitiveField> primitiveFields,
			AbstractField containerClassField, String containerAlias) {
		this.classType = classType;
		this.primitiveFields = primitiveFields;
		this.containerClass = containerClass;
		this.containerAlias = containerAlias;
		this.containerClassField = containerClassField;
	}

	public AbstractField getContainerClassField() {
		return containerClassField;
	}

	public void setContainerClassField(AbstractField containerClassField) {
		this.containerClassField = containerClassField;
	}

	public Class<C> getContainerClass() {
		return containerClass;
	}

	public void setContainerClass(Class<C> containerClass) {
		this.containerClass = containerClass;
	}

	public String getContainerAlias() {
		return containerAlias;
	}

	public void setContainerAlias(String containerAlias) {
		this.containerAlias = containerAlias;
	}

	public List<PrimitiveField> getPrimitiveFields() {
		return primitiveFields;
	}

	public void setPrimitiveFields(List<PrimitiveField> primitiveFields) {
		this.primitiveFields = primitiveFields;
	}

	public Class<T> getClassType() {
		return classType;
	}

	public void setClassType(Class<T> classType) {
		this.classType = classType;
	}
}
