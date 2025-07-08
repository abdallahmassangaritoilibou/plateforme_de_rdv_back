package com.mon_rdv.config;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mon_rdv.model.Availability;
import com.mon_rdv.model.User;
import com.mon_rdv.repository.AvailabilityRepository;
import com.mon_rdv.repository.UserRepository;

@Configuration
public class DataSeedConfig {

    @Bean
    public CommandLineRunner seedAvailabilities(
            AvailabilityRepository availabilityRepo,
            UserRepository userRepo,
            BCryptPasswordEncoder passwordEncoder
    ) {
        return args -> {
            // 1) On ne seed que si la table availabilities est vide
            if (availabilityRepo.count() == 0) {
                
                User pro = userRepo.findById(1L).orElseGet(() -> {
                    User u = new User();
                    u.setForename("Jean");
                    u.setSurname("Dupont");
                    u.setEmail("jean.dupont@example.com");
                    // mot de passe encoded
                    u.setPassword(passwordEncoder.encode("changeme"));
                    return userRepo.save(u);
                });

                // 3) Génération de 10 créneaux de 30 min à partir de maintenant
                LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
                List<Availability> slots = IntStream.range(0, 10)
                    .mapToObj(i -> {
                        Availability a = new Availability();
                        a.setUser(pro);
                        a.setStartTime(now.plusMinutes(i * 30));
                        a.setEndTime(  now.plusMinutes((i + 1) * 30) );
                        // isBooked est false par défaut
                        return a;
                    })
                    .collect(Collectors.toList());

                // 4) Sauvegarde de tous les slots
                availabilityRepo.saveAll(slots);
                System.out.println("✓ 10 créneaux seedés pour Jean Dupont");
            }
        };
    }
}
