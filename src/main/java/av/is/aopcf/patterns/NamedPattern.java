package av.is.aopcf.patterns;

import av.is.aopcf.ParameterMapper;
import com.google.common.collect.Lists;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class NamedPattern {
    
    private final Method method;
    private final String pattern;
    
    public NamedPattern(Method method, String pattern) {
        this.method = method;
        this.pattern = pattern;
    }
    
    public List<PatternEntry> getPatterns() {
        Class<?>[] parameters = method.getParameterTypes();
        List<String> arguments = Lists.newArrayList(pattern.split(" "))
                .stream()
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());
        
        if(parameters.length != arguments.stream().filter(s -> s.contains(":")).count() && method.getAnnotation(ParameterMapper.class) != null) {
            throw new IllegalArgumentException("Mapper's parameters and pattern have to be same!");
        }
        
        AtomicInteger increment = new AtomicInteger(0);
        return arguments.stream().map(pattern -> {
            if(pattern.contains(":")) {
                int i = increment.getAndIncrement();
                return new PatternEntry<>(null, pattern.split(":")[1], parameters[i]);
            } else {
                return new PatternEntry<>(pattern, null, String.class);
            }
        }).collect(Collectors.toList());
    }
}
