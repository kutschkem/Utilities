package interfaces;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Target({CONSTRUCTOR, METHOD})
@Retention(RUNTIME)
@Documented
public @interface SideEffects {
		boolean value();
}
