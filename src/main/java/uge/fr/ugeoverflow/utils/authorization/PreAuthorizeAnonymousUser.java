package uge.fr.ugeoverflow.utils.authorization;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize("hasAnyAuthority('vote:read','comment:read','answer:read','question:read')")
public @interface PreAuthorizeAnonymousUser {

}
