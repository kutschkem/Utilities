package kutschke.utility.proxy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * marks a variable as delegate. Only one field should have this annotation, otherwise only the first one
 * implementing a certain interface will be resolved by {@link ProxyFactory#resolveDelegation(Class, Object, Class[])}
 * This annotation can be used with fields and public, no argument methods
 * @author Michael Kutschke
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Delegate {

}
