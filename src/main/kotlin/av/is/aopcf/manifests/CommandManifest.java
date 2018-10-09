package av.is.aopcf.manifests;

import av.is.aopcf.Command;
import av.is.aopcf.internal.CommandContext;
import av.is.aopcf.internal.impl.CommandContextImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;

import java.lang.reflect.Method;

public class CommandManifest extends AbstractModule {
    
    private final Method method;
    private final Command command;
    
    CommandManifest(Method method, Command command) {
        this.method = method;
        this.command = command;
    }
    
    @Provides Command command() {
        return command;
    }
    
    @Override
    protected void configure() {
        bind(CommandContext.class).to(CommandContextImpl.class);
        bind(Method.class).annotatedWith(Names.named("command")).toInstance(method);
    }
}
