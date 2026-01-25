package com.mssousa.auth.application.service.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mssousa.auth.domain.exception.DomainException;
import com.mssousa.auth.domain.model.shared.IdGenerator;
import com.mssousa.auth.domain.model.user.Email;
import com.mssousa.auth.domain.model.user.Password;
import com.mssousa.auth.domain.model.user.User;
import com.mssousa.auth.domain.model.user.UserId;
import com.mssousa.auth.domain.model.user.Username;
import com.mssousa.auth.domain.repository.UserRepository;
import com.mssousa.auth.domain.service.EmailSender;
import com.mssousa.auth.domain.service.MasterUserPolicy;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MasterUserPolicy masterUserPolicy;
    private final IdGenerator idGenerator;
    private final EmailSender emailSender;

    @Transactional
    public User createUser(String username, String email, String password, String name, boolean master, User creator) {
        // Valida se quem está criando tem permissão para criar MASTER
        if (master) {
            if (creator == null || !masterUserPolicy.canPromoteToMaster(creator)) {
                throw new DomainException("Apenas usuários MASTER podem criar outros usuários MASTER.");
            }
        }

        // Validações de unicidade
        if (userRepository.existsByUsername(new Username(username))) {
            throw new DomainException("Username '" + username + "' já está em uso.");
        }

        if (userRepository.existsByEmail(new Email(email))) {
            throw new DomainException("Email '" + email + "' já está em uso.");
        }

        Long id = idGenerator.generate();

        // Criação do usuário
        User newUser = new User(
            UserId.of(id),
            new Username(username),
            new Email(email),
            Password.fromPlainText(password),
            master,
            com.mssousa.auth.domain.model.user.UserStatus.ACTIVE,
            name
        );
        
        User savedUser = userRepository.save(newUser);
        
        emailSender.send(savedUser.getEmail().value(), "Bem-vindo ao Auth Server", "Olá " + savedUser.getName() + ", seu cadastro foi realizado com sucesso.");
        
        return savedUser;
    }
    
    // Sobrecarga para criação inicial ou por sistema (sem creator explícito se não for master)
    @Transactional
    public User createUser(String username, String email, String password, String name) {
        return createUser(username, email, password, name, false, null);
    }

    @Transactional
    public User updateUser(Long userId, String newName, String newEmail) {
        User user = findByIdOrThrow(userId);

        // Se email mudou, validar unicidade
        if (!user.getEmail().value().equals(newEmail)) {
            if (userRepository.existsByEmail(new Email(newEmail))) {
                throw new DomainException("Email '" + newEmail + "' já está em uso.");
            }
        }

        user.updateProfile(newName, newEmail);
        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = findByIdOrThrow(userId);

        if (!user.verifyPassword(currentPassword)) {
            throw new DomainException("Senha atual incorreta.");
        }

        user.changePassword(Password.fromPlainText(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public User promoteToMaster(Long userId, User currentUser) {
        if (!masterUserPolicy.canPromoteToMaster(currentUser)) {
            throw new DomainException("Apenas usuários MASTER podem promover outros usuários.");
        }

        User targetUser = findByIdOrThrow(userId);
        
        // Política de validação de mudança de status
        if (!masterUserPolicy.canChangeMasterStatus(currentUser, targetUser, true)) {
             throw new DomainException("Operação não permitida pela política de segurança.");
        }

        targetUser.promoteToMaster();
        return userRepository.save(targetUser);
    }

    @Transactional
    public User demoteFromMaster(Long userId, User currentUser) {
        if (!masterUserPolicy.canDemoteFromMaster(currentUser)) {
            throw new DomainException("Apenas usuários MASTER podem rebaixar outros usuários.");
        }

        User targetUser = findByIdOrThrow(userId);
        
        if (!masterUserPolicy.canChangeMasterStatus(currentUser, targetUser, false)) {
             throw new DomainException("Operação não permitida pela política de segurança.");
        }

        targetUser.demoteFromMaster();
        return userRepository.save(targetUser);
    }

    @Transactional
    public User activateUser(Long userId) {
        User user = findByIdOrThrow(userId);
        user.activate();
        return userRepository.save(user);
    }

    @Transactional
    public User disableUser(Long userId) {
        User user = findByIdOrThrow(userId);
        user.disable();
        return userRepository.save(user);
    }

    @Transactional
    public User blockUser(Long userId) {
        User user = findByIdOrThrow(userId);
        user.block();
        return userRepository.save(user);
    }
    
    @Transactional(readOnly = true)
    public Optional<User> findById(Long userId) {
        return userRepository.findById(UserId.of(userId));
    }
    
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(new Username(username));
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(new Email(email));
    }

    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = findByIdOrThrow(userId);
        userRepository.deleteById(user.getId());
    }

    // Helper privado para buscar ou lançar exceção
    private User findByIdOrThrow(Long userId) {
        return userRepository.findById(UserId.of(userId))
            .orElseThrow(() -> new DomainException("Usuário não encontrado com ID: " + userId));
    }
}
