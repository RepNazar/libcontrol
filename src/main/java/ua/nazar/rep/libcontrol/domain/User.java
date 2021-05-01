package ua.nazar.rep.libcontrol.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id","username", "email", "active"})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Username cannot be empty")
    private String username;
    @NotBlank(message = "Password cannot be empty")
    private String password;

    private boolean active;


    @Email(message = "Email is not correct")
    @NotBlank(message = "Email cannot be empty")
    private String email;
    private String activationCode;

    //TODO Learn CascadeTypes and change this one
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Book> books;

    //TODO Maybe replace Set with Single role
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    public boolean isClient() {
        return roles.contains(Role.ROLE_CLIENT);
    }

    public boolean isLibrarian() {
        return roles.contains(Role.ROLE_LIBRARIAN);
    }

    public boolean isManager() {
        return roles.contains(Role.ROLE_MANAGER);
    }

    public boolean isDirector() {
        return roles.contains(Role.ROLE_DIRECTOR);
    }
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> orders;

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

}
