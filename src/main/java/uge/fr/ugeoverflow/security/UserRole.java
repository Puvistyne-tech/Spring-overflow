package uge.fr.ugeoverflow.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static uge.fr.ugeoverflow.security.UserPermission.*;

public enum UserRole {

    ADMIN(Set.of(
            QUESTION_READ,
            QUESTION_WRITE,
            QUESTION_DELETE,
            ANSWER_READ,
            ANSWER_WRITE,
            ANSWER_DELETE,
            COMMENT_READ,
            COMMENT_WRITE,
            COMMENT_DELETE,
            USER_READ,
            USER_WRITE,
            VOTE_READ,
            VOTE_WRITE
    )),

    USER_AUTH(Set.of(
            QUESTION_READ,
            QUESTION_WRITE,
            ANSWER_READ,
            ANSWER_WRITE,
            COMMENT_READ,
            COMMENT_WRITE,
            VOTE_READ,
            VOTE_WRITE
    )),

    USER_ANONYMOUS(Set.of(
            QUESTION_READ,
            ANSWER_READ,
            COMMENT_READ,
            VOTE_READ
    ));

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return permissions;
    }

    public List<GrantedAuthority> getAuthoritiesList() {
        var list= getPermissions().stream()
                .map(userPermission -> (GrantedAuthority) new SimpleGrantedAuthority(userPermission.getPermission()))
                .collect(Collectors.toList());
        list.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return list;
    }

    public String getAuthorityString() {
        var res = getPermissions().stream()
                .map(UserPermission::getPermission)
                .collect(Collectors.joining(" "));

        return res + " ROLE_"+ this.name();
    }
}
