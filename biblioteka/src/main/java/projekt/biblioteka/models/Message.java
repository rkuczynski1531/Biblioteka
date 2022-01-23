package projekt.biblioteka.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Opis nie może być pusty")
    @Column(length = 3000)
    private String text;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiverId;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User senderId;

    private LocalDateTime date;

    public Message(User receiverId, User senderId) {
        this.receiverId = receiverId;
        this.senderId = senderId;
    }

}
