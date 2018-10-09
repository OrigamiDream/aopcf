package av.is.aopcf.example;

import av.is.aopcf.Command;
import av.is.aopcf.CommandRepository;
import av.is.aopcf.ParameterMapper;

@CommandRepository
public class ExampleRepository2 {
    
    @ParameterMapper
    public void a() {
    }
    
    @ParameterMapper
    public void b() {
    }
    
    @Command("")
    public void command() {
    }
    
}
