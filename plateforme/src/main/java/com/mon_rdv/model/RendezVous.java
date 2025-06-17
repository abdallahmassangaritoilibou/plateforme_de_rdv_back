package com.mon_rdv.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "rendez_vous")
public class RendezVous {
 @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //relation ManyToOne avec User (plusieurs RDV par utilisateur)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "availability", nullable = false)
    private LocalDateTime availability;
    
    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "modification_date", nullable = false)
    private LocalDateTime modificationDate;

    // Constructeurs
    public RendezVous() {}
    public RendezVous(User user, LocalDateTime availability, String status) {
        this.user = user;
        this.availability = availability;
        this.status = status;
        this.dateCreation = LocalDateTime.now();
        this.modificationDate = LocalDateTime.now();
    }
    // Getters et Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public LocalDateTime getAvailability() {
        return availability;
    }
    public void setAvailability(LocalDateTime availability) {
        this.availability = availability;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
    public LocalDateTime getModificationDate() {
        return modificationDate;
    }
    public void setModificationDate(LocalDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }
    @PrePersist
    private void onCreate() {
        this.dateCreation = LocalDateTime.now();
        this.modificationDate = LocalDateTime.now();
    }
    @PreUpdate
    private void onUpdate() {
        this.modificationDate = LocalDateTime.now();
    }
    @Override
    public String toString() {
        return "RendezVous{" +
                "id=" + id +
                ", user=" + user +
                ", availability='" + availability + '\'' +
                ", status='" + status + '\'' +
                ", dateCreation=" + dateCreation +
                ", modificationDate=" + modificationDate +
                '}';
    }
}