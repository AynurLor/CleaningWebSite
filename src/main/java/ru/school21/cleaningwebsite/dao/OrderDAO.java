package ru.school21.cleaningwebsite.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.school21.cleaningwebsite.models.OrderClient;

import java.util.Calendar;
import java.util.Date;

@Service
@Component
public class OrderDAO {
    private final OrderRepository orderRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public OrderDAO(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;

    }

    public void saveOrder(OrderClient orderClient) {
        System.out.println(orderClient);
        orderRepository.save(orderClient);

    }
    public void updateStatus(Integer Id, String status) {
        orderRepository.updateOrderStatus(Id, status);
    }

    public void updateOrder(String phone_number, String status, Double summa) {
        orderRepository.updateOrderStatusAndAmount(phone_number, status, summa);
    }
    public OrderClient getOrder(Integer id) {
        OrderClient order = entityManager.find(OrderClient.class, id);
        return orderRepository.getReferenceById(id);
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

    public void updateOrderStatus(Integer orderId, String newStatus) {
        OrderClient order = entityManager.find(OrderClient.class, orderId);
        order.setStatus(newStatus);
        entityManager.merge(order);
    }

    public void updateOrderAmount(Integer orderId, Double newAmount) {
        OrderClient order = entityManager.find(OrderClient.class, orderId);
        order.setAmount(newAmount);
        entityManager.merge(order);
    }

    public void updateAmount(Integer id, Double amount) {
        orderRepository.updateOrderAmount(id, amount);
    }
}
