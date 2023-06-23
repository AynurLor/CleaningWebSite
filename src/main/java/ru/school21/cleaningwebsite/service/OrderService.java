package ru.school21.cleaningwebsite.service;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.school21.cleaningwebsite.models.OrderClient;

import java.sql.Date;

@Service
@Component
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void saveOrder(OrderClient orderClient) {
        System.out.println(orderClient);
        orderRepository.save(orderClient);
//                .addOrder(
//                orderClient.getAmount(),
//                orderClient.getName(),
//                orderClient.getNumberPhone(),
//                orderClient.getOrderDate(),
//                orderClient.getStatus()
//        );
    }
    public void updateOrder(String phone_number, String status, Double summa) {
        orderRepository.updateOrderStatusAndAmount(phone_number, status, summa);
    }
}
