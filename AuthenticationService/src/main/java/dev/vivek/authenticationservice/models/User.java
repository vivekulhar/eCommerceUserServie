package dev.vivek.authenticationservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class User extends BaseModel{
    private String email;
    private String password;

    /*@OneToOne(mappedBy = "user")
    private Session session;*/
    @ManyToMany(fetch= FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();
}
