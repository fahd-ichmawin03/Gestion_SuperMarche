package net.projetmgsi.gestionsupermarche.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity

@Getter
@Setter
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // ⚠️ Constructeur par défaut requis par JPA
    public User() {}

    // Constructeur pratique
    public User(Long id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // getters et setters...
}
