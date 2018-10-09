package av.is.aopcf.internal;

import javax.inject.Inject;
import java.util.Set;

public class Repositories {
    
    @Inject Set<Repository> repositories;
    
    public void prints() {
        System.out.println("repositories: " + repositories.size());
        repositories.forEach(Repository::prints);
    }
    
}
