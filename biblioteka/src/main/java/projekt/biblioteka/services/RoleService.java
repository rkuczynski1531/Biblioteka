package projekt.biblioteka.services;

import org.springframework.stereotype.Service;
import projekt.biblioteka.models.Role;
import projekt.biblioteka.repositories.RoleRepository;

import java.util.List;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getRoles(){
        return roleRepository.findAll();
    }

    public Role getRoleById(int id){
        return roleRepository.findById(id).orElse(null);
    }
}
