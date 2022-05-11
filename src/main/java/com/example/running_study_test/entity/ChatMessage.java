package com.example.running_study_test.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chat_message")
public class ChatMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "room_id")
  @JsonIgnore
  private Room room;

  private String message;

  private String sender;

  public Room getRoom() {
    return room;
  }

  public void setRoom(Room room) {
    this.room = room;
  }
}