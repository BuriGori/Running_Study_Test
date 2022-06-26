package com.example.running_study_test.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "gps_message")
public class GpsMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long roomId;

    private Long senderId;

    private String name;

    private Double latitude;

    private Double longitude;
}
