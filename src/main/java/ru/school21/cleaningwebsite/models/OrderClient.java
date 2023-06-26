package ru.school21.cleaningwebsite.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Entity
@Table(name="orderclient")
public class OrderClient {
//    public enum String {
//        CREATED,
//        APPROVED,
//        COMPLETED,
//        FAILED
//    }
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name="name")
    private String name;
//    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private String status;
    @Pattern(regexp = "\\d{10}", message = "Invalid phone number")
    @Column(name="numberphone")
    private String numberPhone;
//    @DateTimeFormat(pattern = "\\w{4}-\\w{2}-\\w{2}$")
    @Column(name="orderdate")
            private java.util.Date orderDate;
    @Column(name="amount")
    private Double amount;

    public OrderClient() {
    }

    public OrderClient(Integer id, String status, String numberPhone, Double amount) {
        this.id = id;
        this.status = status;
        this.numberPhone = numberPhone;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public java.util.Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(java.util.Date orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "OrderClient{" +
                "id=" + id +
                ", status=" + status +
                ", numberPhone='" + numberPhone + '\'' +
                ", amount=" + amount +
                '}';
    }
}