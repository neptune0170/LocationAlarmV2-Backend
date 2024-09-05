package com.locationalarm.Events.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "attendee")
@Entity
public class Attendee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;

    @Column(name = "user_email")
    private String userEmail;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventHeader eventHeader;
}
