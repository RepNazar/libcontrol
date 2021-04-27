package ua.nazar.rep.libcontrol.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_CLIENT, ROLE_LIBRARIAN, ROLE_MANAGER, ROLE_DIRECTOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
