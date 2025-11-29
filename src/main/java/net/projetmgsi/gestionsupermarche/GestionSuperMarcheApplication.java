package net.projetmgsi.gestionsupermarche;

import net.projetmgsi.gestionsupermarche.user.Role;
import net.projetmgsi.gestionsupermarche.user.User;
import net.projetmgsi.gestionsupermarche.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class GestionSuperMarcheApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionSuperMarcheApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner init(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new User(null, "admin", encoder.encode("1234"), Role.ADMIN));
                repo.save(new User(null, "caissier", encoder.encode("1234"), Role.CAISSIER));
            }
        };
    }
}
