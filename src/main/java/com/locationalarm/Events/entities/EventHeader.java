package com.locationalarm.Events.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "event_header")
@Entity
public class EventHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(name = "event_name",nullable = false)
    private String eventName;

    @Column(name="admin_email",nullable = false)
    private String adminEmail;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "event_end_time")
    private Date eventEndTime;

    @Column(name="is_AOT_enable")
    private boolean isAOTEnable;

    private String latitude;
    private String longitude;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id")
    private List<Attendee> attendeeList;
}
