package av.is.aopcf.utils.inspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Avis Network on 2018-06-24.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Inspect {
    
    boolean inline() default false;
    
    boolean colon() default false;
    
}
