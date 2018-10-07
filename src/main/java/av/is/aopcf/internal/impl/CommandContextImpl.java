package av.is.aopcf.internal.impl;

import av.is.aopcf.Command;
import av.is.aopcf.internal.CommandContext;
import com.google.common.base.Objects;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.Method;

public class CommandContextImpl implements CommandContext {
    
    private final Method method;
    private final Command command;
    
    @Inject CommandContextImpl(@Named("command") Method method, Command command) {
        this.method = method;
        this.command = command;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(method, command);
    }
    
    @Override
    public void prints() {
        System.out.println(" - method: " + method.toString());
        System.out.println(" - command: " + command.toString());
    }
}
