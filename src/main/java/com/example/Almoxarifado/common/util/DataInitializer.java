package com.example.Almoxarifado.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.Almoxarifado.common.enums.TipoUsuarioEnum;
import com.example.Almoxarifado.model.Usuario;
import com.example.Almoxarifado.repository.UsuarioRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        String emailAdmin = "admin@admin.com";

        if(usuarioRepository.findByEmail(emailAdmin) == null) {
            String senhaCriptografada = new BCryptPasswordEncoder().encode("admin");
            Usuario admin = new Usuario(emailAdmin, senhaCriptografada, "Admin", TipoUsuarioEnum.ADMINISTRADOR, "000.000.000-00");

            usuarioRepository.save(admin);
        }

        System.out.println("=================================================");
        System.out.println("   USUÁRIO ADMIN PRÉ-CADASTRADO COM SUCESSO!     ");
        System.out.println("   Login: " + emailAdmin);
        System.out.println("   Senha: admin");
        System.out.println("=================================================");
    }
    
}
