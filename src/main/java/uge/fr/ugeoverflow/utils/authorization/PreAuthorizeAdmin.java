package uge.fr.ugeoverflow.utils.authorization;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
//@PreAuthorize("hasAnyAuthority('user:delete','quesion:delete','answer:delete','comment:delete','vote:delete')")
@PreAuthorize("hasAnyRole('ADMIN')")
public @interface PreAuthorizeAdmin {

}
