import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Reads the Video_Games.html file from the Resources folder,
 * builds {@link Game} objects, and exposes three sorted views.
 */
public class Parser {

    /** All games loaded once {@link #setUp()} is called. */
    private static final List<Game> games = new ArrayList<>();

    // sort helper

    /** Alphabetical A → Z (natural order) */
    public List<Game> sortByName() {
        List<Game> sorted = new ArrayList<>(games);
        sorted.sort(Game::compareTo);
        return sorted;
    }

    /** Highest rating first */
    public List<Game> sortByRating() {
        List<Game> sorted = new ArrayList<>(games);
        sorted.sort(Game.BY_RATING_DESC);
        return sorted;
    }

    /** Highest price first (i.e. most expensive) */
    public List<Game> sortByPrice() {
        List<Game> sorted = new ArrayList<>(games);
        sorted.sort(Game.BY_PRICE_ASC.reversed());
        return sorted;
    }

    // parsing

    /**
     * Parse the HTML exactly once and populate {@link #games}.
     * @throws IOException if the resource cannot be read
     */
    public void setUp() throws IOException {

        if (!games.isEmpty()) return;

        // 1. Load the file from the class-path (src/Resources or resources/)
        try (InputStream in = Parser.class
                .getClassLoader()
                .getResourceAsStream("Video_Games.html")) {    // <<— just the file name

            if (in == null)
                throw new IOException("Video_Games.html not found on the class-path!");

            Document doc = Jsoup.parse(in, StandardCharsets.UTF_8.name(), "");

            // 2. Each game card has class="game" on the outer div
            Elements cards = doc.select("div.game");

            // 3. Build Game objects
            for (Element card : cards) {

                // name is in the <h3 class="game-name"> element
                String name = optText(card, "h3.game-name");

                // rating like "4.8/5" → 4.8
                double rating = parseDoubleSafely(optText(card, "span.game-rating"));

                // price like "91 €" → 91
                int price = parseIntSafely(optText(card, "span.game-price"));

                if (!name.isEmpty()) {
                    games.add(new Game(name, rating, price));
                }
            }
        }
    }

    // helper
    /** Safe text extraction: empty string if selector missing */
    private static String optText(Element root, String cssQuery) {
        Element el = root.selectFirst(cssQuery);
        return el == null ? "" : el.text().trim();
    }

    /** Converts "4.8/5" → 4.8   (returns 0.0 on failure) */
    private static double parseDoubleSafely(String raw) {
        String cleaned = raw.replace(',', '.')
                .replaceAll("[^0-9.]", "");
        return cleaned.isEmpty() ? 0.0 : Double.parseDouble(cleaned);
    }

    /** Converts "91 €" → 91   (returns 0 on failure) */
    private static int parseIntSafely(String raw) {
        String cleaned = raw.replaceAll("[^0-9]", "");
        return cleaned.isEmpty() ? 0 : Integer.parseInt(cleaned);
    }
}
