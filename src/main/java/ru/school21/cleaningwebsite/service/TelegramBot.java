package ru.school21.cleaningwebsite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.school21.cleaningwebsite.config.BotConfig;
import ru.school21.cleaningwebsite.models.User;


@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    final BotConfig config;
    @Autowired
    final UserService userService;

    public TelegramBot(BotConfig config, UserService userService) {
        this.config = config;
        this.userService = userService;
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
                default:
                    sendMessage(chatId, "null");
                    break;
            }
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
//        System.out.println(user);
        userService.saveUser(user);

//        System.out.println(userService.getAllUser());
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
