package com.company.bot;

import com.company.steam_api.SteamApiWorker;
import com.company.steam_api.UserStats;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Map;

public class Bot extends TelegramLongPollingBot {

    private static final String BOT_USERNAME = System.getenv("TELEGRAM_BOT_USERNAME");
    private static final String BOT_TOKEN = System.getenv("TELEGRAM_BOT_KEY");

    private SteamApiWorker apiWorker;

    public void setApiWorker(SteamApiWorker apiWorker) {
        this.apiWorker = apiWorker;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String text = message.getText();
        Long chatId = message.getChatId();

        sendMessage(text, chatId);
    }

    private String checkForStartCommand(String text) {
        if (!"/start".equals(text)) {
            return getResultMessage(text);
        }
        return "Hello, its " + BOT_USERNAME + "." +
                "\nI can show you your CS:GO stats." +
                "\nTo begin send me your steam64ID!";
    }

    private String getResultMessage(String messageText)  {
        if (apiWorker == null) {
            throw new IllegalStateException("SteamApiWorker cannot be null!");
        }

        try {
            String stats = apiWorker.getStats(messageText);
            UserStats userStats = apiWorker.mapStats(stats);
            Map<String, Long> userStatsMap = apiWorker.convertToMap(userStats);
            return apiWorker.createResultMessage(userStatsMap);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error! Check your steamID" +
                    "\nTo begin send me your steam64ID!\n\n" +
                    "To view your stats you must set your game data to public by going to your\n" +
                    "Steam Profile --> Edit Profile --> Privacy Settings --> " +
                    "Set: 'My profile: Public' and 'Game details: public'";
        }
    }

    private void sendMessage(String messageText, Long chatId) {
        String resultMessage = checkForStartCommand(messageText);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(resultMessage);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
