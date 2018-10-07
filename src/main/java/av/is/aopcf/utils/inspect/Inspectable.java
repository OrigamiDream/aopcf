package av.is.aopcf.utils.inspect;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Avis Network on 2018-06-24.
 */
public interface Inspectable {
    
    default String inspectDepth(int depth) {
        depth++;
        
        StringBuilder builder = new StringBuilder();
        if(depth == 1) {
            builder.append("\n");
        }
        builder.append(inspectTabs(depth - 1)).append(getClass().getSimpleName());
        try {
            List<Field> fields = annotatedFields();
            if(fields.size() > 0) {
                builder.append(" = {\n");
            }
            for(int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                field.setAccessible(true);
            
                Inspect inspect = field.getAnnotation(Inspect.class);
                String colon = inspect.colon() ? ": " : " = ";
                builder.append(inspectTabs(depth)).append(field.getName()).append(colon);
                
                Object value = field.get(this);
                if(value == null) {
                    builder.append("NULL");
                } else if(value instanceof String) {
                    String str = value.toString();
                    if(str.contains("\n")) {
                        str = str.replaceAll("\n", "\n" + inspectTabs(depth + 1));
                    }
                    builder.append("'").append(str).append("'");
                } else if(value instanceof Inspectable) {
                    builder.append("{\n");
                    builder.append(((Inspectable) value).inspectDepth(depth + 1));
                    builder.append(inspectTabs(depth)).append("}");
                } else {
                    builder.append(value.toString());
                }
                if(i != fields.size() - 1) {
                    builder.append(",");
                }
                builder.append("\n");
            }
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }
        builder.append(inspectTabs(depth - 1)).append("}\n");
        return builder.toString();
    }
    
    default String inspectTabs(int depth) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < depth; i++) {
            builder.append("    ");
        }
        return builder.toString();
    }
    
    default String inspect() {
        return inspectDepth(0);
    }
    
    default List<Field> annotatedFields() {
        return new ArrayList<>(Arrays.asList(getClass().getDeclaredFields()))
                .stream()
                .filter(f -> f.getAnnotation(Inspect.class) != null)
                .collect(Collectors.toList());
    }
    
    public abstract class Impl implements Inspectable {
        
        @Override
        public String toString() {
            return inspect();
        }
    }
    
}
