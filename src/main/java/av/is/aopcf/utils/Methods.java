package av.is.aopcf.utils;

import com.google.common.collect.Lists;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class Methods {
    
    private Methods() {
    }
    
    public static <T> List<Method> annotatedMethods(Class<T> type, Class<? extends Annotation> annotation) {
        return Lists.newArrayList(type.getDeclaredMethods())
                .stream()
                .filter(method -> method.getAnnotation(annotation) != null)
                .collect(Collectors.toList());
    }
    
}
