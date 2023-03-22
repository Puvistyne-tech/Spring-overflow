package uge.fr.ugeoverflow.utils.authorization;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
//@PreAuthorize("hasAnyAuthority('question:write','answer:write','comment:write','vote:write')")
@PreAuthorize("hasAnyRole('USER_AUTH')")
public @interface PreAuthorizeAuthUser {

}
