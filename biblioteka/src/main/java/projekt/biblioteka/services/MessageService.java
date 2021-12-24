package projekt.biblioteka.services;

import org.springframework.stereotype.Service;
import projekt.biblioteka.models.Message;
import projekt.biblioteka.repositories.MessageRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void saveMessage(Message message){
        messageRepository.save(message);
    }
}
