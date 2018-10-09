package av.is.aopcf.binders;

import av.is.aopcf.utils.Fields;
import av.is.aopcf.utils.Methods;
import com.google.common.collect.Sets;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

public class ComponentBinder {
    
    private final Injector injector;
    private final Binder binder;
    
    public ComponentBinder(Injector injector, Binder binder) {
        this.injector = injector;
        this.binder = binder;
    }
    
    public OptionalTransformBinder<Method> newMethod(Class<?> type) {
        checkNotNull(type, "type");
        return new MethodBinder(type);
    }
    
    public OptionalTransformBinder<Class<?>> newClass(Collection<Class<?>> classes) {
        checkNotNull(classes, "classes");
        return new ClassBinder(classes);
    }
    
    public OptionalTransformBinder<Field> newField(Class<?> type) {
        checkNotNull(type, "type");
        return new FieldBinder(type);
    }
    
    public interface TransformBinder<T1> {
        
        <A extends Annotation, T> InternalBinder<T> transform(Class<A> annotation, Class<T> to, InternalFunction<T1, A, Module> map);
        
        @SuppressWarnings("unchecked")
        default <A extends Annotation, T> List<T> reform(Injector injector, Stream<? extends AnnotatedElement> stream, Class<A> annotation, Class<T> to, InternalFunction<T1, A, Module> map) {
             return stream.map(element -> {
                 A a = element.getAnnotation(annotation);
                 return injector.createChildInjector(map.apply((T1) element, a)).getInstance(to);
             }).collect(Collectors.toList());
        }
    }
    
    public abstract class OptionalTransformBinder<T1> implements TransformBinder<T1> {
        
        private boolean allowed = true;
        
        public OptionalTransformBinder<T1> allow(boolean allow) {
            this.allowed = allow;
            return this;
        }
        
        <A extends Annotation, T> List<T> optionalReform(Injector injector, Stream<? extends AnnotatedElement> stream, Class<A> annotation, Class<T> to, InternalFunction<T1, A, Module> map) {
            if(!allowed && stream.count() > 0) {
                throw new IllegalStateException("'" + to.getSimpleName() + "' is not supported.");
            }
            return reform(injector, stream, annotation, to, map);
        }
    }
    
    class MethodBinder extends OptionalTransformBinder<Method> {
        
        private final Class<?> type;
    
        MethodBinder(Class<?> type) {
            this.type = type;
        }
        
        public <A extends Annotation, T> InternalBinder<T> transform(Class<A> annotation, Class<T> to, InternalFunction<Method, A, Module> map) {
            return new InternalBinder<>(optionalReform(injector, Methods.annotatedMethods(type, annotation).stream(), annotation, to, map), to, binder);
        }
    }
    
    class FieldBinder extends OptionalTransformBinder<Field> {
        
        private final Class<?> type;
    
        FieldBinder(Class<?> type) {
            this.type = type;
        }
    
        @Override
        public <A extends Annotation, T> InternalBinder<T> transform(Class<A> annotation, Class<T> to, InternalFunction<Field, A, Module> map) {
            return new InternalBinder<>(optionalReform(injector, Fields.annotatedFields(type, annotation).stream(), annotation, to, map), to, binder);
        }
    }
    
    class ClassBinder extends OptionalTransformBinder<Class<?>> {
        
        private final Collection<Class<?>> classes;
    
        ClassBinder(Collection<Class<?>> classes) {
            this.classes = classes;
        }
    
        @Override
        public <A extends Annotation, T> InternalBinder<T> transform(Class<A> annotation, Class<T> to, InternalFunction<Class<?>, A, Module> map) {
            return new InternalBinder<>(optionalReform(injector, classes.stream(), annotation, to, map), to, binder);
        }
    }
    
    public class InternalBinder<T> {
        
        private final List<T> list;
        private final Class<T> type;
        private final Binder binder;
        
        InternalBinder(List<T> list, Class<T> type, Binder binder) {
            this.list = list;
            this.type = type;
            this.binder = binder;
        }
        
        @SuppressWarnings("unchecked")
        public void bind() {
            binder.bind((TypeLiteral<Set<T>>) TypeLiteral.get(Types.setOf(type))).toInstance(Sets.newHashSet(list));
        }
    }
    
    @FunctionalInterface
    public interface InternalFunction<T1, T2, R> {
    
        R apply(T1 t1, T2 t2);
        
    }
}
