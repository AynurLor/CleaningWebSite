package ru.school21.cleaningwebsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.school21.cleaningwebsite.config.BotConfig;
import ru.school21.cleaningwebsite.dao.OrderDAO;
import ru.school21.cleaningwebsite.dao.UserDAO;
import ru.school21.cleaningwebsite.models.OrderClient;
import ru.school21.cleaningwebsite.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


enum STATE {
    NONE,
    START,
    EDIT,
    WRITE_AMOUNT_FOR_ORDER,
    STATISTIC,
    HELP,
}

@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    final BotConfig config;
    @Autowired
    final UserDAO userDAO;
    @Autowired
    OrderDAO orderDAO;

    private STATE status = STATE.NONE;

    private Map<Long, OrderClient> isWaitingForOrderNumber = new HashMap<>();

    final static String HELP_TEXT = "Бот необходим для отслеживания текущих заказов.\n" +
            "Описание команд:\n" +
            "/start - для начала диалога\n" +
            "/edit - для изменения статуса заказа и суммы конечного заказа\n" +
            "/statistic - для отслеживания статистики продаж за 30 дней\n" +
            "/help - краткое описание бота\n";

    public TelegramBot(BotConfig config, UserDAO userDAO, OrderDAO orderDAO) {
        this.config = config;
        this.userDAO = userDAO;
        this.orderDAO = orderDAO;
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
                case "/edit":
                    status = STATE.EDIT;
                    sendMessage(chatId, "Please enter the order number:");
                    break;
                case "/statistic":
                    status = STATE.STATISTIC;
                    sendMessage(chatId, "Statistics for the last 30 days: " +
                            orderDAO.getAmountOrderForMouth().toString() + " rub");
                    break;
                case "/help":
                    status = STATE.HELP;
                    sendMessage(chatId, HELP_TEXT);
                    break;
                default:
                    trackingOrderNumber(chatId, messageText);
                    break;
            }

        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callbackData.equals("APPROVED") ||
                callbackData.equals("COMPLETED") ||
                callbackData.equals("FAILED")) {
                    OrderClient client = isWaitingForOrderNumber.get(chatId);
                    try {
                    orderDAO.updateStatus(client.getId(), callbackData);

                    } catch (Exception e) {
                        e.printStackTrace();
                        sendMessage(chatId, "Sorry, the order has already been completed");
                        isWaitingForOrderNumber.remove(chatId);
                        status = STATE.NONE;
                    }
                    sendMessage(chatId, "Enter the order amount, otherwise send 0");
                    status = STATE.WRITE_AMOUNT_FOR_ORDER;
            }
        }


    }


    private void trackingOrderNumber(Long ChatId, String messageText) {

        SendMessage message = new SendMessage();
        if (status == STATE.EDIT) {
            Integer orderNumber = Integer.parseInt(messageText);
            isWaitingForOrderNumber.put(ChatId, orderDAO.getOrder(orderNumber));
            message.setChatId(String.valueOf(ChatId));
            message.setText("Please, set a new order status from the list below");

            if (status == STATE.EDIT) {

                InlineKeyboardMarkup markupKeyboard =  new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowsLine = new ArrayList<>();
                var approvedButton = new InlineKeyboardButton();
                approvedButton.setText("APPROVED");
                approvedButton.setCallbackData("APPROVED");

                var completedButton = new InlineKeyboardButton();
                completedButton.setText("COMPLETED");
                completedButton.setCallbackData("COMPLETED");

                var failedButton = new InlineKeyboardButton();
                failedButton.setText("FAILED");
                failedButton.setCallbackData("FAILED");

                rowsLine.add(approvedButton);
                rowsLine.add(completedButton);
                rowsLine.add(failedButton);

                rowsInline.add(rowsLine);
                markupKeyboard.setKeyboard(rowsInline);
                message.setReplyMarkup(markupKeyboard);
                }

            } else if (status == STATE.WRITE_AMOUNT_FOR_ORDER) {
                OrderClient client = isWaitingForOrderNumber.get(ChatId);
                Double amount = Double.parseDouble(messageText);
                orderDAO.updateAmount(client.getId(), amount);
                status = STATE.NONE;
                sendMessage(ChatId, "Order is update");
            }
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
    }
    public void startCommandReceived(long chatId, String name) {
        String answer = "Привет, " + name + ", приятно познокомиться!" +
                "\nДля описания комманд введи: /help";
        status = STATE.START;
        sendMessage(chatId, answer);
    }
    public void sendMessage(long chatId, String textToSend) {
        System.out.println(status.toString());
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
        return userDAO.getUser(IdTelegram) != null ? true: false;
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

        userDAO.saveUser(user);
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
