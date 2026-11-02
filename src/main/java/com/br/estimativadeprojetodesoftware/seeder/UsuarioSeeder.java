package com.br.estimativadeprojetodesoftware.seeder;

import com.br.estimativadeprojetodesoftware.model.Usuario;
import com.br.estimativadeprojetodesoftware.repository.IUsuarioRepository;
import com.br.estimativadeprojetodesoftware.service.SenhaHashService;
import java.time.LocalDateTime;
import java.util.UUID;

/** Cria o usuário administrador de demonstração, se ainda não existir. */
public class UsuarioSeeder {

    public static final String EMAIL_ADMIN = "kaua@email.com";
    private static final UUID ID_ADMIN = UUID.fromString("533ee667-86a2-47c7-8f99-b8fb18f19fd8");

    private final IUsuarioRepository repository;

    public UsuarioSeeder(IUsuarioRepository repository) {
        this.repository = repository;
    }

    public void executar() {
        if (repository.buscarPorEmail(EMAIL_ADMIN).isEmpty()) {
            repository.salvar(new Usuario(ID_ADMIN, "kaua", EMAIL_ADMIN, SenhaHashService.gerarHash("A@#bb"), LocalDateTime.now()));
        }
    }
}
