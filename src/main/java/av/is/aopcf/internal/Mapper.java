package av.is.aopcf.internal;

import av.is.aopcf.patterns.PatternEntry;

import java.util.List;

public interface Mapper {

    void prints();
    
    Object tryInvoke(List<PatternEntry> patternEntries);
    
    void setRepository(Repository repository);
    
    Class<?> returnType();
    
}
