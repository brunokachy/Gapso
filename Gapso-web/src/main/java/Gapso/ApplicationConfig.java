/**
 *Class Name: ApplicationConfig
 *Project Name: Gapso-web
 *Developer: Onyedika Okafor (ookafor@morphinnovations.com)
 *Version Info:
 *Create Date: Apr 13, 2017 3:43:55 PM
 *(C)Morph Innovations Limited 2017. Morph Innovations Limited Asserts its right to be known
 *as the author and owner of this file and its contents.
 */
package Gapso;

import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * @author Onyedika Okafor (ookafor@morphinnovations.com)
 */
@ApplicationPath("service")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(Gapso.CORSFilter.class);
        resources.add(Gapso.GapsoService.class);
    }

}
