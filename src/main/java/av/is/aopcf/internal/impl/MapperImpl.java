package av.is.aopcf.internal.impl;

import av.is.aopcf.internal.Mapper;
import av.is.aopcf.internal.Repository;
import av.is.aopcf.patterns.NamedPattern;
import av.is.aopcf.patterns.PatternEntry;
import com.google.common.base.Objects;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MapperImpl implements Mapper {
    
    private final Method method;
    private final String pattern;
    private final List<PatternEntry> entries;
    private Repository repository;
    
    @Inject MapperImpl(@Named("mapper") Method method, String pattern) {
        this.method = method;
        this.pattern = pattern;
        this.entries = new NamedPattern(method, pattern).getPatterns();
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(method, pattern);
    }
    
    @Override
    public void prints() {
        System.out.println(" - method: " + method.toString());
        System.out.println(" - pattern: " + pattern);
    }
    
    @Override
    public Object tryInvoke(List<PatternEntry> patternEntries) {
        if(entries.size() != patternEntries.size()) {
            return null;
        }
        List<Object> arguments = new ArrayList<>();
        for(int i = 0; i < entries.size(); i++) {
            PatternEntry a = entries.get(i);
            PatternEntry b = patternEntries.get(i);
            if(!a.matches(b)) {
                return null;
            }
            if(a.getPattern() != null) {
                arguments.add(b.getArgument());
            }
        }
        method.setAccessible(true);
        try {
            return method.invoke(repository.getInstance(), arguments.toArray());
        } catch(IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public void setRepository(Repository repository) {
        this.repository = repository;
    }
    
    @Override
    public Class<?> returnType() {
        return method.getReturnType();
    }
}
