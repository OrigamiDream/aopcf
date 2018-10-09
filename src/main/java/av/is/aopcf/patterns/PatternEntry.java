package av.is.aopcf.patterns;

import com.google.common.base.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

public class PatternEntry<T> {
    
    @Nullable private final T argument;
    @Nullable private final String pattern;
    private final Class<?> parameterType;
    
    PatternEntry(@Nullable T argument, @Nullable String pattern, Class<T> parameterType) {
        this.argument = argument;
        this.pattern = pattern;
        this.parameterType = parameterType;
    }
    
    public PatternEntry(@Nonnull T argument) {
        this(argument, (Class<T>) argument.getClass());
    }
    
    public PatternEntry(@Nonnull T argument, Class<T> parameterType) {
        checkNotNull(argument, "argument");
        
        this.argument = argument;
        this.pattern = null;
        this.parameterType = parameterType;
    }
    
    @Nullable
    public T getArgument() {
        return argument;
    }
    
    @Nullable
    public String getPattern() {
        return pattern;
    }
    
    public Class<?> getParameterType() {
        return parameterType;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(argument, pattern, parameterType);
    }
    
    public boolean matches(PatternEntry argument) {
        boolean matches;
        if(getArgument() != null) {
            matches = getArgument().equals(argument.getArgument());
        } else {
            matches = true;
        }
        matches &= getParameterType().equals(argument.getParameterType());
        return matches;
    }
}
