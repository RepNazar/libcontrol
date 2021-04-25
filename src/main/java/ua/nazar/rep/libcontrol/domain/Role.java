package ua.nazar.rep.libcontrol.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, LIBRARIAN, MANAGER, DIRECTOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
