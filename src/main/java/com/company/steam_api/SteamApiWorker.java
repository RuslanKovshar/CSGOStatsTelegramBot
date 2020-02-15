package com.company.steam_api;

import com.company.steam_api.editor.AbstractEditor;
import com.company.steam_api.editor.HSPercentageEditor;
import com.company.steam_api.editor.KDRationEditor;
import com.company.steam_api.editor.WeaponsEditor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SteamApiWorker {

    private static final String API_URL = "http://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v0002/" +
            "?appid=730&key=%s&steamid=%s";
    private static final String KEY = System.getenv("TELEGRAM_KEY");

    public String getStats(String steamId) throws IOException {
        String requestURL = String.format(API_URL, KEY, steamId);

        URL obj = new URL(requestURL);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    public UserStats mapStats(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        return mapper.readValue(json, UserStats.class);
    }

    public Map<String, Long> convertToMap(UserStats userStats) {
        Map<String, Long> stats = new HashMap<>();
        Arrays.stream(userStats.getStats()).forEach(stat -> stats.put(stat.getName(), stat.getValue()));
        return stats;
    }

    public String createResultMessage(Map<String, Long> stats) {
        StringBuilder builder = new StringBuilder();
        AbstractEditor editor = new KDRationEditor(new HSPercentageEditor(new WeaponsEditor()));
        editor.edit(stats,builder);
        return builder.toString();
    }
}
