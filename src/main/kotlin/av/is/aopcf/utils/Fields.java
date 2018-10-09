package av.is.aopcf.utils;

import com.google.common.collect.Lists;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class Fields {
    
    private Fields() {
    }
    
    public static <T> List<Field> annotatedFields(Class<T> type, Class<? extends Annotation> annotation) {
        return Lists.newArrayList(type.getDeclaredFields())
                .stream()
                .filter(field -> field.getAnnotation(annotation) != null)
                .collect(Collectors.toList());
    }
    
}
