package projekt.biblioteka.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekt.biblioteka.models.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {


}
