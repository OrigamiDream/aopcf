package av.is.aopcf.manifests;

import av.is.aopcf.CommandRepository;
import av.is.aopcf.binders.ComponentBinder;
import av.is.aopcf.example.ExampleRepository;
import av.is.aopcf.example.ExampleRepository2;
import av.is.aopcf.internal.Repositories;
import av.is.aopcf.internal.Repository;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class CoreManifest extends AbstractModule {
    
    private final Injector injector;
    private final Class<?>[] classes;
    
    public CoreManifest(Injector injector, Class<?>... classes) {
        this.injector = injector;
        this.classes = classes;
    }
    
    @Override
    protected void configure() {
        ComponentBinder binder = new ComponentBinder(injector, binder());
        binder.newClass(Lists.newArrayList(classes))
                .transform(CommandRepository.class, Repository.class, (element, annotation) -> new RepositoryManifest(injector, element))
                .bind();
    }
    
    public static void main(String args[]) {
        Injector injector = Guice.createInjector();
        Repositories repositories = injector.createChildInjector(new CoreManifest(injector, ExampleRepository.class, ExampleRepository2.class)).getInstance(Repositories.class);
        
        repositories.prints();
    }
}
