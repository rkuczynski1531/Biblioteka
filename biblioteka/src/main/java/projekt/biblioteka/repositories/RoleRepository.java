package projekt.biblioteka.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekt.biblioteka.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByName(String name);
}
