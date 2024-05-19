package com.example.petProject.entity;

import com.example.petProject.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Integer id;
    private String username;
    private String password;
    private boolean enabled;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "UserRoles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private Set<Role> roles = new HashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       /* return permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPrivilegeType()))
                .collect(Collectors.toList());*/
       /* return roles.forEach(role -> {
            role.getFeatures().stream()
                    .map(permission ->  new SimpleGrantedAuthority(permission.getPrivilegeType()))
                    .collect(Collectors.toList());*/
        List<GrantedAuthority> authorities = new ArrayList<>();
        /*roles.forEach(role -> {
            authorities.add((GrantedAuthority) role.getFeatures().stream().map(permission -> new SimpleGrantedAuthority(permission.getPrivilegeType())).collect(Collectors.toList()));
        });*/

        roles.forEach(role -> {
            // Convert each permission to a GrantedAuthority and add to authorities list
            List<GrantedAuthority> roleAuthorities = role.getFeatures().stream()
                    .filter(Feature::isActive)
                    .map(permission -> new SimpleGrantedAuthority(permission.getPrivilegeType()))
                    .collect(Collectors.toList());
            authorities.addAll(roleAuthorities);
        });



      /*  return (Collection<? extends GrantedAuthority>) (Collection<? extends GrantedAuthority>) roles.stream()
                .map(role -> role.getFeatures().stream().map(permission -> new SimpleGrantedAuthority(permission.getPrivilegeType()))
                .collect(Collectors.toList())).collect(Collectors.toList());*/
        return authorities;
    }

    public String getModules(){
       /* return String.join(",", permissions.stream()
                .map(x -> x.getModule().getModuleId().toString())
                .collect(Collectors.toSet()));*/
        return "";
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
