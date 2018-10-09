package av.is.aopcf.example;

public class Example {
    
    private final String name;
    private final int amount;
    
    public Example(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }
    
    public String getName() {
        return name;
    }
    
    public int getAmount() {
        return amount;
    }
    
    @Override
    public String toString() {
        return "Example{name = " + name + ", amount = " + amount + "}";
    }
}
