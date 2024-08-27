package org.example.final_project.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.final_project.util.Gender;
import org.example.final_project.util.UserStatus;
import org.example.final_project.util.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_user")
public class User extends AbstractEntity<Long> implements UserDetails, Serializable {

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "date_of_birth")
  @Temporal(TemporalType.DATE)
  private Date dateOfBirth;

  @Enumerated(EnumType.STRING)
  @Column(name = "gender")
  private Gender gender;

  @Column(name = "phone")
  private String phone;

  @Column(name = "email")
  private String email;

  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  private UserType type;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private UserStatus status;

  @Column(name = "image_url")
  private String imageUrl;

  @JsonIgnore
  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  private Set<UserHasRole> roles = new HashSet<>();

  @JsonIgnore
  @OneToMany(mappedBy = "user")
  private Set<GroupHasUser> groups = new HashSet<>();

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream()
        .map(userHasRole -> new SimpleGrantedAuthority("ROLE_" + userHasRole.getRole().getName()))
        .collect(Collectors.toList());
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
    return UserStatus.ACTIVE.equals(status);
  }
}
