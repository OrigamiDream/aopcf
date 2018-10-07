package av.is.aopcf.internal;

import av.is.aopcf.patterns.PatternEntry;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Repository {
    
    private final Class<?> type;
    private final Object instance;
    private final Set<Mapper> mappers;
    private final Set<CommandContext> commandContexts;
    
    @Inject Repository(Class<?> type, Object instance, Set<Mapper> mappers, Set<CommandContext> commandContexts) {
        this.type = type;
        this.instance = instance;
        this.mappers = mappers.stream().peek(mapper -> mapper.setRepository(this)).collect(Collectors.toSet());
        this.commandContexts = commandContexts;
    }
    
    public Object getInstance() {
        return instance;
    }
    
    void prints() {
        System.out.println("type: " + type.getSimpleName());
        System.out.println("mappers: " + mappers.size());
        mappers.forEach(Mapper::prints);
        System.out.println("commands: " + commandContexts.size());
        commandContexts.forEach(CommandContext::prints);
    
        Object o = getMapped(Lists.newArrayList(new PatternEntry<>("my name"), new PatternEntry<>("plus"), new PatternEntry<>(5, int.class)));
        System.out.println(Optional.ofNullable(o).orElse("null").toString());
    }
    
    public Object getMapped(List<PatternEntry> patternEntries) {
        for(Mapper mapper : mappers) {
            Object object = mapper.tryInvoke(patternEntries);
            if(object != null) {
                return object;
            }
        }
        return null;
    }
    
}
