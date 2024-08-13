package internsafegate.noteapp.service.role;

import internsafegate.noteapp.model.Role;
import internsafegate.noteapp.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{
    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findById(1L).orElseThrow(() -> new RuntimeException("Role USER not found"));
    }
}
