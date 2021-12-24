package projekt.biblioteka.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

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
    public Message() {
    }

    public Message(User receiverId, User senderId) {
        this.receiverId = receiverId;
        this.senderId = senderId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(User receiverId) {
        this.receiverId = receiverId;
    }

    public User getSenderId() {
        return senderId;
    }

    public void setSenderId(User senderId) {
        this.senderId = senderId;
    }
}
