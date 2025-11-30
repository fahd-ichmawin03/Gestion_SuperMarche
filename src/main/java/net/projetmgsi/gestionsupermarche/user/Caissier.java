package net.projetmgsi.gestionsupermarche.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Caissier {

    private String username;
    private String password;

    public Caissier(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
