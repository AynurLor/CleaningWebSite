package ru.school21.cleaningwebsite.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.school21.cleaningwebsite.models.OrderClient;
import ru.school21.cleaningwebsite.models.User;
import ru.school21.cleaningwebsite.service.DefaultEmailService;
import ru.school21.cleaningwebsite.service.OrderService;
import ru.school21.cleaningwebsite.service.TelegramBot;
import ru.school21.cleaningwebsite.service.UserService;

import javax.mail.MessagingException;
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
        createOrder(name, number_phone);
        //  Отправялем всем юзерам tg  информацию о заказе
        sendMessage(number_phone);

        try {
            service.sendEmail("testemailcleaningwebsite@gmail.com", "application",
                    "name: " + name + "\nnumber_phone: " + number_phone);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return "index";
    }

    private void createOrder(String name, String phone_number) {
        OrderClient orderClient = new OrderClient();
        orderClient.setNumberPhone(phone_number);
        orderClient.setName(name);
        orderClient.setStatus("CREATED");
        orderClient.setAmount(0.0);
        orderClient.setOrderDate(Date.valueOf(LocalDate.now()));
        orderService.saveOrder(orderClient);

    }
    private void updateOrder(String phone_number, String status, Double summa) {
//        orderService.updateOrder("+79991634752", "APPROVED", 100.0);
        orderService.updateOrder(phone_number, status, summa);
    }

    private void sendMessage(String number_phone) {
        for (User user : userService.getAllUser()) {
            bot.sendMessage(user.getIdTelegram(),
                    "new order by phone number: " + number_phone);
        }
    }
}
