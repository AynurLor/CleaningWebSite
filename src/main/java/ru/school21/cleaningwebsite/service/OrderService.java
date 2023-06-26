package ru.school21.cleaningwebsite.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.school21.cleaningwebsite.models.OrderClient;

import javax.swing.text.html.parser.Entity;
import java.util.Calendar;
import java.util.Date;

@Service
@Component
public class OrderService {
    private final OrderRepository orderRepository;


    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;

    }

    public void saveOrder(OrderClient orderClient) {
        System.out.println(orderClient);
        orderRepository.save(orderClient);


    }
    public void updateOrder(String phone_number, String status, Double summa) {
        orderRepository.updateOrderStatusAndAmount(phone_number, status, summa);
    }

    public Double getAmountOrderForMouth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        Date thirtyDaysAgo = calendar.getTime();
        String queryString = "SELECT SUM(o.amount) FROM OrderClient o WHERE o.orderDate >= :thirtyDaysAgo";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("thirtyDaysAgo", thirtyDaysAgo);
        return (Double) query.getSingleResult();
    }

//    public void editeStatusOrder(String status, Double amount) {
//        String queryString = 'update orderClient set status = 'APPROVED', amount = 1500\nwhere id = 1';
//    }
    public void updateOrderStatusAndAmount(Integer orderId, String newStatus, Double newAmount) {
        OrderClient order = entityManager.find(OrderClient.class, orderId);
        order.setStatus(newStatus);
        order.setAmount(newAmount);
        entityManager.merge(order);
    }
}
