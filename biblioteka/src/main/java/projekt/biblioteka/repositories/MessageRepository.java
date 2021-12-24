package projekt.biblioteka.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekt.biblioteka.models.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
