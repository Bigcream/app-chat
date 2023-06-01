//package com.example.appchat.security;
//
//
//import com.example.appchat.model.entity.RoleEntity;
//import com.example.appchat.model.entity.UserEntity;
//import lombok.Data;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
//
//@Data
//public class CustomUserDetails implements UserDetails {
//
//    private final UserEntity user;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
//        Set<RoleEntity> roles = user.getRoles();
//        for (RoleEntity role : roles) {
//            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
//        }
//        return grantedAuthorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return user.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return user.getUsername();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
