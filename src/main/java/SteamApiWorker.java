import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SteamApiWorker {

    private static final String API_URL = "http://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v0002/" +
            "?appid=730&key=%s&steamid=%s";
    private static final String KEY = "63A7093AE2FF9F68F062E9707E5470F7";


    /**
     * @param steamId steam id
     * @return user stats in json
     * @throws IOException if exceptions occurs
     */
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

    public String setMessage(Map<String, Long> stats) {
        Long totalKills = stats.get("total_kills");
        Long totalDeaths = stats.get("total_deaths");
        Long killsHeadshot = stats.remove("total_kills_headshot");

        double ratio = roundValue(totalKills.doubleValue() / totalDeaths);
        double headShotPercentage = roundValue(killsHeadshot * 100. / totalKills);

        StringBuilder builder = new StringBuilder();
        builder.append("K/D ratio: ").append(ratio).append("\n");
        builder.append("Headshot percentage: ").append(headShotPercentage).append("\n\n");

        builder.append("Total kills with:").append("\n");
        stats.entrySet().stream()
                .filter(entry -> entry.getKey().contains("total_kills_"))
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> builder.append(entry.getKey().replace("total_kills_", "").toUpperCase())
                        .append(":\t\t")
                        .append(entry.getValue())
                        .append("\n"));

        return builder.toString();
    }

    private double roundValue(double value) {
        return new BigDecimal(value)
                .setScale(2, RoundingMode.UP)
                .doubleValue();
    }

}
