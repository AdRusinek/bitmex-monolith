package com.rusinek.bitmexmonolith.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rusinek.bitmexmonolith.model.requestlimits.UserRequestLimit;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by Adrian Rusinek on 01.03.2020
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email(message = "Username needs to be an mail")
    @NotBlank(message = "Username is required")
    @Column(unique = true)
    private String username;
    @NotBlank(message = "Please enter your full name.")
    private String fullName;
    @NotBlank(message = "Password field is required.")
    private String password;
    @Transient
    private String confirmPassword;
    private boolean isTokenVerified;
    private Date createdAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private Collection<Account> accounts = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<Alert> alerts = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private UserRequestLimit userRequestLimit;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private TwoFactorToken twoFactorToken;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private RegisterToken registerToken;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    public boolean isEnabled() {
        return isTokenVerified;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }
}
