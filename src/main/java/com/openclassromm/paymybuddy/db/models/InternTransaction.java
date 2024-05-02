package com.openclassromm.paymybuddy.db.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "intern_transactions")
public class InternTransaction {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Size(max = 2)
    @NotNull
    @Column(name = "status", nullable = false, length = 2)
    private String status;

    @Size(max = 350)
    @NotNull
    @Column(name = "label", nullable = false, length = 350)
    private String label;

    @NotNull
    @Column(name = "amount", nullable = false, precision = 5, scale = 2)
    private BigDecimal amount;

    @NotNull
    @Column(name = "taxe", nullable = false, precision = 3, scale = 2)
    private BigDecimal taxe;

    @NotNull
    @Column(name = "id_sender", nullable = false)
    private Integer idSender;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_friendship", nullable = false)
    private Friendship idFriendship;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTaxe() {
        return taxe;
    }

    public void setTaxe(BigDecimal taxe) {
        this.taxe = taxe;
    }

    public Integer getIdSender() {
        return idSender;
    }

    public void setIdSender(Integer idSender) {
        this.idSender = idSender;
    }

    public Friendship getIdFriendship() {
        return idFriendship;
    }

    public void setIdFriendship(Friendship idFriendship) {
        this.idFriendship = idFriendship;
    }
}