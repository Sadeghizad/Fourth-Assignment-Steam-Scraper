import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Reads the Video_Games.html file from the Resources folder,
 * builds {@link Game} objects, and exposes three sorted views.
 */
public class Parser {

    /** All games loaded once is called. */
    private static final List<Game> games = new ArrayList<>();
    private static final List<OscarFilm> films = new ArrayList<>();
    private static final Map<String, List<String>> STUDIO_CACHE = new java.util.concurrent.ConcurrentHashMap<>();
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

    public void setUp(String url) throws IOException {


        Document doc = readDocSmart(url);   // helper shown below


        games.clear();
        films.clear();

        boolean looksLikeGamePage  = !doc.select("div.game").isEmpty();
        boolean looksLikeOscarPage = !doc.select("#firstHeading > span:nth-child(1):contains(Best Picture)").isEmpty();

        if (looksLikeGamePage) {
            Elements cards = doc.select("div.game");
            for (Element card : cards) {
                String name   = optText(card, "h3.game-name");
                double rating = parseDoubleSafely(optText(card, "span.game-rating"));
                int    price  = parseIntSafely   (optText(card, "span.game-price"));
                if (!name.isEmpty())
                    games.add(new Game(name, rating, price));
            }
        }
        else if (looksLikeOscarPage) {
            Elements rows = doc.select("table.wikitable tr");

            String currentYear = "";  // Tracks the year for each block of rows

            for (Element row : rows) {
                // Check if this row has a <th> with the year link
                Element yearHeader = row.selectFirst("th > a[href][title$='in film']");
                if (yearHeader != null) {
                    currentYear = yearHeader.text().trim();  // e.g., "1928/29"
                    continue;  // Skip to next row since this row isn't a movie entry
                }

                // Otherwise, it’s likely a movie row with <td>s
                Elements cells = row.select("td");
                if (cells.size() < 2) continue;  // Skip rows without enough data

                String title = cells.get(0).text().trim();
                String studio = cells.get(1).text().trim();
                List<String> studios = Arrays.asList(studio.split(",\\s*"));

                films.add(new OscarFilm(title, currentYear, studios, "local"));
            }

        } else {
            throw new IOException("Unrecognized page structure for URL/file: " + url);
        }
    }

    private static List<String> fetchStudios(String wikiUrl) throws IOException {

        List<String> cached = STUDIO_CACHE.get(wikiUrl);
        if (cached != null) return cached;

        Document filmDoc = Jsoup
                .connect(wikiUrl)
                .timeout(10000)
                .get();


        Element row = filmDoc.selectFirst(
                "table.infobox tr:has(th:matchesOwn(^\\s*Production company))," +
                        "table.infobox tr:has(th:matchesOwn(^\\s*Production companies))");

        List<String> studios = new ArrayList<>();

        if (row != null) {
            // prefer anchor text; fall back to plain td text
            for (Element a : row.select("td a")) {
                String txt = a.text().trim();
                if (!txt.isEmpty()) studios.add(txt);
            }
            if (studios.isEmpty()) {
                String raw = row.selectFirst("td").text().trim();
                if (!raw.isEmpty()) Collections.addAll(studios, raw.split(",\\s*"));
            }
        }

        if (studios.isEmpty()) studios.add("N/A");


        List<String> unmodifiable = List.copyOf(studios);
        STUDIO_CACHE.put(wikiUrl, unmodifiable);
        return unmodifiable;
    }
    // helper

    private static Document readDocSmart(String src) throws IOException {
        if (src == null || src.isBlank()) src = "Video_Games.html";
        if (src.startsWith("http://") || src.startsWith("https://"))
            return Jsoup.connect(src).get();

        try (InputStream in = Parser.class.getClassLoader().getResourceAsStream(src)) {
            if (in != null)
                return Jsoup.parse(in, StandardCharsets.UTF_8.name(), "");
        }
        return Jsoup.parse(new java.io.File(src), StandardCharsets.UTF_8.name());
    }


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

    public List<OscarFilm> getOscarFilms() {
        return new ArrayList<>(films);
    }
}
