package av.is.aopcf.manifests;

import av.is.aopcf.ParameterMapper;
import av.is.aopcf.internal.Mapper;
import av.is.aopcf.internal.impl.MapperImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;

import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkNotNull;

public class MapperManifest extends AbstractModule {
    
    private final Method method;
    private final ParameterMapper mapper;
    
    MapperManifest(Method method, ParameterMapper mapper) {
        checkNotNull(method, "mapping method");
        checkNotNull(mapper, "mapper");
        
        this.method = method;
        this.mapper = mapper;
    }
    
    @Provides String pattern() {
        return mapper.value();
    }
    
    @Override
    protected void configure() {
        bind(Mapper.class).to(MapperImpl.class);
        bind(Method.class).annotatedWith(Names.named("mapper")).toInstance(method);
    }
}
