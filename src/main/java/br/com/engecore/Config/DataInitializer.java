package br.com.engecore.config; // (Use o pacote que preferir)

import br.com.engecore.Enum.Role;
import br.com.engecore.Enum.Status;
import br.com.engecore.Enum.TipoPessoa;
import br.com.engecore.Repository.UserRepository;
import br.com.engecore.Entity.UserEntity; // (Importe sua entidade de Usuário)
import br.com.engecore.Repository.UserRepository; // (Importe seu repositório de Usuário)
import br.com.engecore.Enum.Role;     // (Importe seu Enum Role)
import br.com.engecore.Enum.Status;   // (Importe seu Enum Status)
import br.com.engecore.Enum.TipoPessoa; // (Importe seu Enum TipoPessoa)

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor // Para injetar as dependências via construtor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // 1. Verifica se o usuário "admin@engecore.com" já existe
        if (userRepository.findByEmail("admin@engecore.com").isEmpty()) {

            System.out.println("Nenhum usuário ADMIN encontrado. Criando usuário padrão...");

            // 2. Define uma senha padrão (NUNCA deixe nula ou em texto)
            String senhaPadrao = "admin123"; // Troque se desejar

            // 3. Cria a nova entidade de usuário
            UserEntity adminUser = new UserEntity();

            adminUser.setNome("Admin");
            adminUser.setEmail("admin@engecore.com");

            // 4. Criptografa a senha antes de salvar
            adminUser.setSenha(passwordEncoder.encode(senhaPadrao));

            adminUser.setTelefone("(11) 99999-9999");

            // Assume que você tem Enums para Status, Role e TipoPessoa
            adminUser.setStatus(Status.STATUS_ATIVO);
            adminUser.setRole(Role.ROLE_ADMIN);
            adminUser.setTipoPessoa(TipoPessoa.FISICA);

            // 5. Salva no banco de dados
            userRepository.save(adminUser);

            System.out.println("Usuário ADMIN criado com sucesso!");
            System.out.println("Email: admin@engecore.com");
            System.out.println("Senha: " + senhaPadrao);

        } else {
            System.out.println("Usuário ADMIN já existe. Nenhuma ação necessária.");
        }
    }
}