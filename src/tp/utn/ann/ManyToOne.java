package tp.utn.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ManyToOne {
	
	String NONE = "";
	
	Class<?> type();
	String att() default NONE;
	String name();
}
