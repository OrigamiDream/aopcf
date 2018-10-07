package av.is.aopcf.manifests;

import av.is.aopcf.Command;
import av.is.aopcf.CommandRepository;
import av.is.aopcf.ParameterMapper;
import av.is.aopcf.binders.ComponentBinder;
import av.is.aopcf.internal.CommandContext;
import av.is.aopcf.internal.Mapper;
import av.is.aopcf.utils.ChainingOptional;
import av.is.aopcf.utils.Types;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;

import static com.google.common.base.Preconditions.checkNotNull;

public class RepositoryManifest extends AbstractModule {
    
    private final Injector injector;
    private final Class<?> type;
    
    RepositoryManifest(Injector injector, Class<?> type) {
        this.injector = injector;
        checkNotNull(type, "repository type");
        
        this.type = type;
    }
    
    @Provides Class<?> repositoryType() {
        return type;
    }
    
    @Provides Object repositoryInstance() {
        return injector.getInstance(type);
    }
    
    @Override
    protected void configure() {
        ChainingOptional<CommandRepository> chaining = Types.annotatedChain(type, CommandRepository.class);
        chaining.ifPresent(commandRepository -> {
            ComponentBinder binder = new ComponentBinder(injector, binder());
            binder.newMethod(type)
                    .allow(commandRepository.mappers())
                    .transform(ParameterMapper.class, Mapper.class, MapperManifest::new)
                    .bind();
            
            binder.newMethod(type)
                    .allow(commandRepository.commands())
                    .transform(Command.class, CommandContext.class, CommandManifest::new)
                    .bind();
            
        }).orElse(() -> {
            throw new IllegalArgumentException("Command repository requires @CommandRepository annotation.");
        });
    }
}
