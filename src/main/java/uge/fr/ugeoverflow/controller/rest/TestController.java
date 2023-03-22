package uge.fr.ugeoverflow.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uge.fr.ugeoverflow.utils.authorization.PreAuthorizeAnonymousUser;

@RestController
public class TestController {


    //Test for authority and role


    @GetMapping("/test/all")
    public ResponseEntity<?> all(@CurrentSecurityContext SecurityContext securityContext) {
        System.out.println("securityContext = " + securityContext);
        return ResponseEntity.ok(securityContext.getAuthentication());
    }

    @GetMapping("/test/an")
    @PreAuthorizeAnonymousUser
    public ResponseEntity<?> anonym(@CurrentSecurityContext SecurityContext securityContext) {
        System.out.println("securityContext = " + securityContext);
        return ResponseEntity.ok(securityContext);
    }

    @GetMapping("/auth/au")
//    @PreAuthorizeAuthUser
    public ResponseEntity<?> auth(@CurrentSecurityContext SecurityContext securityContext) {
        System.out.println("securityContext = " + securityContext);
        return ResponseEntity.ok(securityContext);
    }

    @GetMapping("/admin/ad")
//    @PreAuthorizeAdmin
//    @PreAuthorize("hasAnyAuthority('question:write','answer:write','comment:write','vote:write')")
    public ResponseEntity<?> admin(@CurrentSecurityContext SecurityContext securityContext) {
        System.out.println("securityContext = " + securityContext);
        return ResponseEntity.ok(securityContext);
    }


}
