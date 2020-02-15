import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Map;

public class Bot extends TelegramLongPollingBot {

    private static final String BOT_USERNAME = "CSGOUserStatsBot";
    private static final String BOT_TOKEN = "1079061401:AAEjKQmjMedMLXZq3iPWPoF6Q1nCSwVmWu8";

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
            return apiWorker.setMessage(userStatsMap);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error! Check your steamID";
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
