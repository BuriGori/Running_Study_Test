package com.example.running_study_test.entity;

import java.util.ArrayList;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Room {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private String name;

  @Column(name = "member_count")
  @ColumnDefault("1")
  private int memberCount;

  @Column(name = "ready_count")
  @ColumnDefault("0")
  private int readyCount;

  @Column(name = "admin_id")
  private Long adminId;

  @OneToMany(mappedBy = "room")
  private List<ChatMessage> chatMessageList = new ArrayList<>();

  @OneToMany
  @JoinColumn(name = "participant_member")
  private List<Member> memberList = new ArrayList<>();

  @Builder
  public Room(String name, Long adminId){
    this.name = name;
    this.adminId =  adminId;
  }

  public void addMember(Member member){
    this.memberCount++;
    this.memberList.add(member);
  }

  public void setReadyMembers(){
    readyCount = 0;
  }

  public void addReadyMembers(){
    readyCount++;
  }
}
