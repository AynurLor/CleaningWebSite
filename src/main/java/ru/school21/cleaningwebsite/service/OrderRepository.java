package ru.school21.cleaningwebsite.service;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.school21.cleaningwebsite.models.OrderClient;


public interface OrderRepository extends JpaRepository<OrderClient, Integer> {
    @Transactional
    @Modifying
    @Query("UPDATE OrderClient o SET o.status = :status, o.amount = :amount WHERE o.numberPhone = :number_phone")
    void updateOrderStatusAndAmount(String number_phone, String status, Double amount);
//    @Query(value = "INSERT INTO orderClient (amount, name, numberphone, orderDate, status) VALUES (:amount, :name, :numberphone, :orderDate, :stat)", nativeQuery = true)
//    void addOrder(@Param("amount") Double amount, @Param("name") String name, @Param("numberphone") String numberphone, @Param("orderDate") Date orderDate, @Param("status") OrderClient.RequestState stat);
//    insert into orderclient (amount,name,numberphone,orderdate,status) values (?,?,?,?,?)
//    @Query("SELECT u FROM OrderClient u WHERE u.numberPhone= :idTelegram")
//    User findByIdTelegram(@Param("idTelegram")Integer idTelegram);
//    @Query("insert into OrderClient u (name,numberphone) value()")
}
