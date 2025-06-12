import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class WeatherApp extends JFrame {

    private JTextField cityField;
    private JButton getWeatherButton;
    private JLabel weatherResultLabel;

    public WeatherApp() {
        setTitle("Smart Weather App");
        setSize(500, 400); // Increased size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(230, 240, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new FlowLayout());
        cityField = new JTextField(18);
        getWeatherButton = new JButton("Get Weather");

        inputPanel.add(new JLabel("Enter City:"));
        inputPanel.add(cityField);
        inputPanel.add(getWeatherButton);

        weatherResultLabel = new JLabel("<html><center>Weather info will appear here</center></html>", SwingConstants.CENTER);
        weatherResultLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        getWeatherButton.addActionListener(e -> {
            String city = cityField.getText().trim();
            if (!city.isEmpty()) {
                fetchWeather(city);
            }
        });

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(weatherResultLabel, BorderLayout.CENTER);

        add(panel);
    }

    private void fetchWeather(String city) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            protected Void doInBackground() {
                try {
                    String apiKey = "df7261f338c5468aa23140633251206";
                    String urlString = "http://api.weatherapi.com/v1/current.json?key=" + apiKey + "&q=" + city;

                    java.net.URL url = new java.net.URL(urlString);
                    java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    java.io.BufferedReader reader = new java.io.BufferedReader(
                            new java.io.InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Parse data
                    String result = response.toString();
                    String tempStr = result.split("\"temp_c\":")[1].split(",")[0];
                    String condition = result.split("\"text\":\"")[1].split("\"")[0];
                    String location = result.split("\"name\":\"")[1].split("\"")[0];

                    double temp = Double.parseDouble(tempStr);

                    // Suggestion
                    String suggestion;
                    if (condition.toLowerCase().contains("rain")) {
                        suggestion = "🌧 It's rainy — carry an umbrella or wear a raincoat.";
                    } else if (temp >= 30) {
                        suggestion = "☀️ It's hot — wear light cotton clothes and stay hydrated.";
                    } else if (temp >= 20) {
                        suggestion = "🌤 Pleasant weather — dress comfortably.";
                    } else if (temp >= 10) {
                        suggestion = "🧥 It's a bit chilly — wear a light jacket.";
                    } else {
                        suggestion = "❄️ It's cold — wear warm clothes and stay cozy.";
                    }

                    // Motivational quote
                    String quote = getRandomQuote();

                    String displayText = "<html><center>"
                            + "📍 <b>City:</b> " + location
                            + "<br>🌡 <b>Temperature:</b> " + temp + "°C"
                            + "<br>🌥 <b>Condition:</b> " + condition
                            + "<br><br><b>👕 Outfit Suggestion:</b> " + suggestion
                            + "<br><br><i>💡 Quote of the Day:</i><br>\"" + quote + "\""
                            + "</center></html>";

                    SwingUtilities.invokeLater(() -> weatherResultLabel.setText(displayText));

                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> weatherResultLabel.setText("Error fetching weather."));
                    e.printStackTrace();
                }
                return null;
            }
        };
        worker.execute();
    }

    private String getRandomQuote() {
        String[] quotes = {
                "The best way to get started is to quit talking and begin doing.",
                "Don’t watch the clock; do what it does. Keep going.",
                "Push yourself, because no one else is going to do it for you.",
                "Every day is a new beginning. Take a deep breath and start again.",
                "You don’t have to be perfect, just keep going.",
                "Believe you can and you're halfway there.",
                "Difficult roads often lead to beautiful destinations.",
                "Success is what happens after you’ve survived all your mistakes."
        };
        Random rand = new Random();
        return quotes[rand.nextInt(quotes.length)];
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WeatherApp().setVisible(true);
        });
    }
}
