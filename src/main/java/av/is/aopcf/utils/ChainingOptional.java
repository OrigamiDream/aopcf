package av.is.aopcf.utils;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class ChainingOptional<T> {
    
    private final Optional<T> optional;
    
    public ChainingOptional(Optional<T> optional) {
        this.optional = optional;
    }
    
    public static <T> ChainingOptional<T> of(Optional<T> optional) {
        return new ChainingOptional<>(optional);
    }
    
    public OrElseOptional<T> ifPresent(Consumer<T> consumer) {
        AtomicBoolean passed = new AtomicBoolean(false);
        optional.ifPresent(t -> {
            passed.set(true);
            consumer.accept(t);
        });
        return new OrElseOptional<>(passed);
    }
    
    public class OrElseOptional<T> {
        
        private final AtomicBoolean passed;
    
        private OrElseOptional(AtomicBoolean passed) {
            this.passed = passed;
        }
        
        public void orElse(Runnable runnable) {
            if(!passed.get()) {
                runnable.run();
            }
        }
    }
}
