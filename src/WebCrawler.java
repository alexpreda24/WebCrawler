import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLHandshakeException;
import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.io.IOException;
import java.io.FileWriter;

public class WebCrawler {
    private static final int MAX_PAGES_TO_SEARCH = 10;
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    public static int countPagesVisited = 0;
    private Set<String> pagesVisited = new HashSet<String>();
    private List<String> pagesToVisit = new LinkedList<String>();
    private Set<String> emails = new HashSet<>();

    private Set<String> phone = new HashSet<>();

    public static boolean isValid(String url) {
        /* Try creating a valid URL */
        try {
            new URL(url).toURI();
            return true;
        }
        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }

    private String nextUrl() {
        String nextUrl;
        do {
            nextUrl = this.pagesToVisit.remove(0);

        } while (this.pagesVisited.contains(nextUrl));
        this.pagesVisited.add(nextUrl);
        return nextUrl;

    }

    public int checkBusiness(String x, String st) throws IOException {

        // Connect to the website and parse the HTML
        // Parse the HTML of the website
        try {
            Document doc = Jsoup.connect(x).get();
            // Look for elements that are typically present on company websites
            Elements contactLinks = doc.select("a[href*=contact]");
            Elements aboutLinks = doc.select("a[href*=about]");
            Elements productLinks = doc.select("a[href*=product]");
            Elements serviceLinks = doc.select("a[href*=service]");
            Elements socialLinks = doc.select("a[href*=social]");

// If any of these elements are present, it's a good indication that the website might be a company website
            if (!contactLinks.isEmpty() || !aboutLinks.isEmpty() || !productLinks.isEmpty() || !serviceLinks.isEmpty() || !socialLinks.isEmpty()) {
                return 1;
            } else {
                return 0;
            }
        } catch (UnknownHostException e) {
            return 0;
        } catch (SSLHandshakeException e) {
            return 0;
        } catch (ConnectException e) {
            return 0;
        } catch (HttpStatusException e) {
            return 0;
        } catch (SocketTimeoutException e) {
            return 0;
        }
    }

    public void search(String url, String searchWord) {
        while (this.pagesVisited.size() < MAX_PAGES_TO_SEARCH) {
            String currentUrl;
            SpiderLeg leg = new SpiderLeg();
            if (this.pagesToVisit.isEmpty()) {
                currentUrl = url;
                this.pagesVisited.add(url);
            } else {
                currentUrl = this.nextUrl();
            }
            if (isValid(currentUrl) && leg.crawl(currentUrl)) {
                leg.searchForWord(currentUrl, emails, phone);
            }
            // SpiderLeg
            int i = this.pagesToVisit.size();
            this.pagesToVisit.addAll(leg.getLinks());
            this.pagesToVisit.addAll(leg.getLinks());
            countPagesVisited++;
            if (i == this.pagesToVisit.size()) {
                break;
            }
        }
        System.out.print("The emails from this website are: " + emails);
        System.out.println();
        System.out.println("The phone numbers from this website are: " + phone);
        System.out.println(String.format("**Done** Visited %s web page(s)", this.pagesVisited.size()));

    }

    public static void main(String[] args) {
        File file = new File("sample-websites.csv");
        File file1 = new File("C:\\Users\\alex\\IdeaProjects\\ProiectNou\\output.csv");
        String st;
        try {
            BufferedReader br = new BufferedReader(new java.io.FileReader(file));
            st = br.readLine();
            int i = 0;
            while ((st = br.readLine()) != null) {
                WebCrawler crawler = new WebCrawler();
                String x = "http://www." + st + "/";
                String company = st.split("\\.")[0];
                System.out.println("The company is: " + company);
                i++;
                System.out.println(i + " " + x);
                if (crawler.checkBusiness(x, company) == 1) crawler.search(x, "computer");
                try (FileWriter fw = new FileWriter("output.csv", true);
                     BufferedWriter bw = new BufferedWriter(fw);
                     PrintWriter out = new PrintWriter(bw)) {
                    out.println("For the website: " + x);
                    out.println("The emails from this website are: " + crawler.emails);
                    out.println("The phone numbers from this website are: " + crawler.phone);
                    out.println();
                } catch (IOException e) {
                    //exception handling left as an exercise for the reader
                    e.printStackTrace();
                }
                if (countPagesVisited > 100) {
                    System.out.println("The number of pages visited is: " + countPagesVisited);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
