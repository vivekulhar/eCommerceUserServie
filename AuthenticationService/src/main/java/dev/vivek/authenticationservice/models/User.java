package dev.vivek.authenticationservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class User extends BaseModel{
    private String email;
    private String password;
    private String fullName;
    @OneToOne(mappedBy = "user")
    private Session session;
}
