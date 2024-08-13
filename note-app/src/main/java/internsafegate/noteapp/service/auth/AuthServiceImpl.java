package internsafegate.noteapp.service.auth;

import internsafegate.noteapp.dto.request.auth.AuthDTO;
import internsafegate.noteapp.dto.request.auth.LoginDTO;
import internsafegate.noteapp.dto.response.auth.AuthResponse;
import internsafegate.noteapp.exception.DataNotFoundException;
import internsafegate.noteapp.exception.UsernameAlreadyExistsException;
import internsafegate.noteapp.model.Role;
import internsafegate.noteapp.model.Token;
import internsafegate.noteapp.model.Users;
import internsafegate.noteapp.repository.RoleRepository;
import internsafegate.noteapp.repository.TokenRepository;
import internsafegate.noteapp.repository.UserRepository;
import internsafegate.noteapp.security.JwtService;
import internsafegate.noteapp.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepo;
    private final TokenRepository tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(AuthDTO authDTO) throws Exception{
        if(!authDTO.getUsername().isBlank() && userRepo.findByUsername(authDTO.getUsername()).isPresent()){
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        Role userRole = roleService.getUserRole();

        Users newUser = Users.builder()
                .username(authDTO.getUsername())
                .password(passwordEncoder.encode(authDTO.getPassword()))
                .email(authDTO.getEmail())
                .dateOfBirth(authDTO.getDateOfBirth())
                .role(userRole)
                .active(true)
                .build();

        Users savedUser = userRepo.save(newUser);
        String jwtToken = jwtService.generateToken(savedUser);
        String refreshToken = jwtService.generateRefreshToken(savedUser);
        saveUserToken(savedUser, jwtToken);


        return AuthResponse.builder()
                .message("register Successfully")
                .accessToken(jwtToken)
                .tokenType("BEARER")
                .refreshToken(refreshToken)
                .username(savedUser.getUsername())
                .id(savedUser.getId())
                .build();
    }

    @Override
    public AuthResponse login(LoginDTO loginDTO) throws Exception {
        Optional<Users> optionalUser = Optional.empty();
        if(loginDTO.getUsername() != null) {
            optionalUser = userRepo.findByUsername(loginDTO.getUsername());
        }

        if(optionalUser.isEmpty()){
            throw new DataNotFoundException("don't find user");
        }

        Users userDetail = optionalUser.get();

        if(!userDetail.isActive()) {
            throw new DataNotFoundException("user is locked");
        }

        if(!passwordEncoder.matches(loginDTO.getPassword() ,userDetail.getPassword())) {
            throw new DataNotFoundException("user wrong password");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword(),
                        userDetail.getAuthorities());

        authenticationManager.authenticate(authenticationToken);

        String jwtToken = jwtService.generateToken(userDetail);
        String refreshToken = jwtService.generateRefreshToken(userDetail);
        revokeAllUserTokens(userDetail);
        saveUserToken(userDetail, jwtToken);

        return AuthResponse.builder()
                .message("Login Successfully")
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .tokenType("BEARER")
                .username(userDetail.getUsername())
                .id(userDetail.getId())
                .build();
    }

    private void revokeAllUserTokens(Users user) {
        List<Token> validUserTokens = tokenRepo.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenRepo.saveAll(validUserTokens);
    }

    private void saveUserToken(Users users, String jwtToken) {
        Token token = Token.builder()
                .user(users)
                .token(jwtToken)
                .tokenType("BEARER")
                .expired(false)
                .revoked(false)
                .build();
        tokenRepo.save(token);
    }
}
