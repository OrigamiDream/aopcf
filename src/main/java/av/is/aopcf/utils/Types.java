package av.is.aopcf.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

public class Types {
    
    private Types() {
    }
    
    public static <T extends Annotation> Optional<T> annotated(AnnotatedElement element, Class<T> annotation) {
        return Optional.ofNullable(element.getAnnotation(annotation));
    }
    
    public static <T extends Annotation> ChainingOptional<T> annotatedChain(AnnotatedElement element, Class<T> annotation) {
        return chain(annotated(element, annotation));
    }
    
    public static <T> ChainingOptional<T> chain(Optional<T> optional) {
        return ChainingOptional.of(optional);
    }
    
}
