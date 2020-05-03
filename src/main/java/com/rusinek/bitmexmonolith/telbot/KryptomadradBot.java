package com.rusinek.bitmexmonolith.telbot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Created by Adrian Rusinek on 02.05.2020
 **/
public class KryptomadradBot extends TelegramLongPollingBot {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new KryptomadradBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update.getMessage().getText());

        if (update.getMessage().hasText()) {
            if (update.getMessage().getText().equals("siema")) {
                System.out.println("==============================================================================");
                SendMessage message = new SendMessage();
                message.setChatId(update.getMessage().getChatId());
                message.setText("elo");

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getBotToken() {
        return "1096664184:AAHEIwxf91XTCzQ3CtPN7usRyuf0lEQh21o";
    }


    @Override
    public String getBotUsername() {
        return "kryptomadrad_bot";
    }

}
