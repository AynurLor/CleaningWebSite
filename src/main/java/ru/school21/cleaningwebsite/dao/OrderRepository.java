package ru.school21.cleaningwebsite.dao;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.school21.cleaningwebsite.models.OrderClient;

import java.util.List;


public interface OrderRepository extends JpaRepository<OrderClient, Integer> {
    @Transactional
    @Modifying
    @Query("UPDATE OrderClient o SET o.status = :status, o.amount = :amount WHERE o.numberPhone = :number_phone")
    void updateOrderStatusAndAmount(String number_phone, String status, Double amount);

    @Transactional
    @Modifying
    @Query("UPDATE OrderClient o SET o.status = :status WHERE o.id = :id")
    void updateOrderStatus(Integer id, String status);

    @Transactional
    @Modifying
    @Query("UPDATE OrderClient o SET o.amount = :amount WHERE o.id = :id")
    void updateOrderAmount(Integer id, Double amount);

    @Query(value = "SELECT * FROM OrderClient", nativeQuery = true)
    List<OrderClient> findAllOrderClient();

}
