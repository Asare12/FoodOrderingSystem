package com.davidasare.FoodOrderingSystem.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name="app_user")
public class User {

    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @Id
    private long id;
    private String name;
    private String email;
    private String password;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    private Boolean locked = false;
    private Boolean enabled = false;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

}
