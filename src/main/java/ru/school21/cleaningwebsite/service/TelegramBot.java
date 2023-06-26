package ru.school21.cleaningwebsite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.school21.cleaningwebsite.config.BotConfig;
import ru.school21.cleaningwebsite.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    final BotConfig config;
    @Autowired
    final UserService userService;
    @Autowired OrderService orderService;

    private Map<Long, Boolean> isWaitingForOrderNumber = new HashMap<>();

    final static String HELP_TEXT = "Бот необходим для отслеживания текущих заказов.\n" +
            "Описание команд:\n" +
            "/start - для начала диалога\n" +
            "/edit - для изменения статуса заказа и суммы конечного заказа\n" +
            "/statistic - для отслеживания статистики продаж за 30 дней\n" +
            "/help - краткое описание бота\n";

    public TelegramBot(BotConfig config, UserService userService, OrderService orderService) {
        this.config = config;
        this.userService = userService;
        this.orderService = orderService;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get message"));
        listOfCommands.add(new BotCommand("/edit", "Change order status"));
        listOfCommands.add(new BotCommand("/statistic", "show sales statistics"));
        listOfCommands.add(new BotCommand("/help", "learn more about the bot"));
        SetMyCommands commands= new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null);
        try {
            this.execute(commands);
        } catch (TelegramApiException e) {
            System.err.println("error" + e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText())  {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String request = update.getMessage().getText();

            if (!UserIsPresent(Integer.parseInt(chatId.toString())))
                saveDataBase(update);

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/clear":
//                    chatgpt.clearDialog();
                    sendMessage(chatId, "Диалог удален");
                    break;
                case "/edit":
                    isWaitingForOrderNumber.put(chatId, true);
                    sendMessage(chatId, "Please enter the order number:");
                    break;
                case "/statistic":
                    sendMessage(chatId, orderService.getAmountOrderForMouth().toString());
                    break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                default:
                    sendMessage(chatId, "null");
                    trackingOrderNumber(chatId, messageText);
                    break;
            }
        }

    }

    private void trackingOrderNumber(Long ChatId, String messageText) {
        if (isWaitingForOrderNumber.containsKey(ChatId) && isWaitingForOrderNumber.get(ChatId)) {
            int orderNumber = Integer.parseInt(messageText);
            sendMessage(ChatId, messageText);
            isWaitingForOrderNumber.remove(ChatId);
        }
    }
    public void startCommandReceived(long chatId, String name) {
        String answer = "Привет, " + name + ", приятно познокомиться!" +
                "\nДля сброса диалога ввееди: /clear";
        sendMessage(chatId, answer);
    }
    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean UserIsPresent(Integer IdTelegram) {
        return userService.getUser(IdTelegram) != null ? true: false;
    }
    private void saveDataBase(Update update) {
        User user = new User();
        Message message = update.getMessage();
        String messageText = update.getMessage().getText();
        Long chatId = message.getChatId();

        user.setIdTelegram(Integer.parseInt(chatId.toString()));
        user.setUserName(message.getChat().getUserName());
        user.setFirstName(message.getChat().getFirstName());
        user.setLastName(message.getChat().getLastName());

        userService.saveUser(user);
    }


    @Override
    public String getBotToken() {
        return config.getToken();
    }
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

}
