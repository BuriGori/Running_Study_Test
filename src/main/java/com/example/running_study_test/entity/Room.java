package com.example.running_study_test.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "room")
    @ToString.Exclude
    private List<ChatMessage> chatMessageList = new java.util.ArrayList<>();

    public void addMessage(ChatMessage chatMessage) {
        chatMessageList.add(chatMessage);
    }

    public void removeMessage(ChatMessage chatMessage) {
        chatMessageList.remove(chatMessage);
    }
}
