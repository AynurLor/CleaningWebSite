package ru.school21.cleaningwebsite.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.school21.cleaningwebsite.models.OrderClient;
import ru.school21.cleaningwebsite.models.User;
import ru.school21.cleaningwebsite.service.DefaultEmailService;
import ru.school21.cleaningwebsite.dao.OrderService;
import ru.school21.cleaningwebsite.dao.UserService;

import java.sql.Date;
import java.time.LocalDate;


@org.springframework.stereotype.Controller
public class Controller {
    @Autowired
    final DefaultEmailService service;
    @Autowired
    TelegramBot bot;
    @Autowired
    final UserService userService;
    @Autowired
    final OrderService orderService;

    public Controller(DefaultEmailService service, UserService userService, OrderService orderService) {
        this.service = service;
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/hello")
    public String mainPage() {
        return "index";
    }
    @PostMapping("/submit-form")
    public String calculate(@RequestParam("name")String name, @RequestParam("number_phone")String number_phone){
        System.out.println("Hello " + name + " with number " + number_phone);

        //  Добаляем новую запись в  БД
        OrderClient data_client = createOrder(name, number_phone);
        //  Отправялем всем юзерам tg  информацию о заказе
        sendMessage("id: " + data_client.getId()
                + "\nName: " + data_client.getName()
                + "Phone_number: " + data_client.getNumberPhone());

//      email расслка
//        try {
//            service.sendEmail("testemailcleaningwebsite@gmail.com", "application",
//                    "name: " + name + "\nnumber_phone: " + number_phone);
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
        return "index";
    }

    private OrderClient createOrder(String name, String phone_number) {
        OrderClient orderClient = new OrderClient();
        orderClient.setNumberPhone(phone_number);
        orderClient.setName(name);
        orderClient.setStatus("CREATED");
        orderClient.setAmount(0.0);
        orderClient.setOrderDate(Date.valueOf(LocalDate.now()));
        orderService.saveOrder(orderClient);
        return orderClient;
    }
    private void updateOrderStatusAndAmount(Integer orderId, String newStatus, Double newAmount) {
        updateOrderStatusAndAmount(orderId, newStatus, newAmount);
    }

    private void sendMessage(String number_phone) {
        for (User user : userService.getAllUser()) {
            bot.sendMessage(user.getIdTelegram(),
                    "new order by phone number: " + number_phone);
        }
    }
}
