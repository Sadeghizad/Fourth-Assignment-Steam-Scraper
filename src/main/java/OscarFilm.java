import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Immutable data holder for an Academy‑Award Best Picture winner.
 */
public final class OscarFilm implements Comparable<OscarFilm> {

    /** Film title, e.g. "Wings" */
    private final String title;

    /** Award year text as shown in the nav‑box (can be a range such as "1927–1928"). */
    private final String awardYear;

    /** Production studios (one or many). */
    private final List<String> studios;

    /** Absolute Wikipedia URL for the film (e.g. "https://en.wikipedia.org/wiki/Wings_(1927_film)"). */
    private final String wikiUrl;

    //constructor
    public OscarFilm(String title, String awardYear, List<String> studios, String wikiUrl) {
        this.title     = Objects.requireNonNull(title).trim();
        this.awardYear = Objects.requireNonNull(awardYear).trim();
        this.studios   = Collections.unmodifiableList(new java.util.ArrayList<>(Objects.requireNonNull(studios)));
        this.wikiUrl   = Objects.requireNonNull(wikiUrl).trim();
    }

    //getters
    public String getTitle()     { return title;     }
    public String getAwardYear() { return awardYear; }
    public List<String> getStudios() { return studios; }
    public String getWikiUrl()   { return wikiUrl;   }

    //toString
    @Override public String toString() {
        return String.format("%-45s | %8s | %s", title, awardYear, String.join(", ", studios));
    }

    //compare / equals
    @Override public int compareTo(OscarFilm o) {
        return this.title.compareToIgnoreCase(o.title);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OscarFilm)) return false;
        OscarFilm that = (OscarFilm) o;
        return title.equals(that.title) && awardYear.equals(that.awardYear) && studios.equals(that.studios);
    }

    @Override public int hashCode() { return Objects.hash(title, awardYear, studios); }
}
