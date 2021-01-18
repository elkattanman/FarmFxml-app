package com.elkattanman.farmFxml;

import com.elkattanman.farmFxml.domain.Capital;
import com.elkattanman.farmFxml.domain.User;
import com.elkattanman.farmFxml.repositories.CapitalRepository;
import com.elkattanman.farmFxml.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class Bootstrap implements CommandLineRunner {

    private final CapitalRepository capitalRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if(!capitalRepository.findById(1).isPresent()){
            log.info("Creating new profile");
            capitalRepository.save(new Capital(1,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0));
        }
        if(userRepository.findAll().size()==0){
            log.info("Creating admin user");
            userRepository.save(new User(null,"admin","admin","admin@mail.com"));
        }
    }
}