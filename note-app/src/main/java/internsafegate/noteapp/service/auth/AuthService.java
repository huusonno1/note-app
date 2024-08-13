package internsafegate.noteapp.service.auth;

import internsafegate.noteapp.dto.request.auth.AuthDTO;
import internsafegate.noteapp.dto.request.auth.LoginDTO;
import internsafegate.noteapp.dto.response.auth.AuthResponse;

public interface AuthService {
    AuthResponse register(AuthDTO authDTO) throws Exception;

    AuthResponse login(LoginDTO loginDTO) throws Exception;
}
