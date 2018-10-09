package av.is.aopcf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE)
public @interface CommandRepository {
    
    /**
     * Indicates which the repository you can declare the mapping methods on.
     */
    boolean mappers() default true;
    
    /**
     * Indicates which the repository you can declare the command methods on.
     */
    boolean commands() default true;
    
}
