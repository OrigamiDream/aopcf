package av.is.aopcf.example;

import av.is.aopcf.Command;
import av.is.aopcf.CommandRepository;
import av.is.aopcf.ParameterMapper;

@CommandRepository
public class ExampleRepository {
    
    @ParameterMapper(":name :amount")
    public Example c(String name, int amount) {
        System.out.println("c()");
        return new Example(name, amount);
    }
    
    @ParameterMapper(":amount1 :amount2")
    public Example d(int amount1, int amount2) {
        System.out.println("d()");
        return new Example(String.valueOf(amount1), amount2);
    }
    
    @ParameterMapper(":name plus :amount")
    public Example f(String name, int amount) {
        System.out.println("f()");
        return new Example(name, amount + amount);
    }
    
    @ParameterMapper(":name1 :name2")
    public Example e(String name1, String name2) {
        System.out.println("e()");
        int i = 0;
        try {
            i = Integer.parseInt(name2);
        } catch(Exception ignored) {
        }
        return new Example(name1, i);
    }
    
    @Command("example :name :amount")
    public void command(Example example) {
    
    }
    
}
