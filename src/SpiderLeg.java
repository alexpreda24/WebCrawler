import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderLeg {
    // We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private List<String> links = new LinkedList<String>();
    private Document htmlDocument;


    /**
     * This performs all the work. It makes an HTTP request, checks the response, and then gathers
     * up all the links on the page. Perform a searchForWord after the successful crawl
     *
     * @param url - The URL to visit
     * @return whether or not the crawl was successful
     */
    public boolean crawl(String url) {
        try {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;
            Element elements = htmlDocument.getElementById("[id*=contact]");
//            Element linksOnPage = htmlDocument.("contact");
//            if(linksOnPage == null) linksOnPage = htmlDocument.getElementById("contact-us");

            return true;
        } catch (IOException ioe) {
            // We were not successful in our HTTP request
            return false;
        }
    }

    /**
     * Performs a search on the body of on the HTML document that is retrieved. This method should
     * only be called after a successful crawl.
     *
     * @param searchWord - The word or string to look for
     * @return whether or not the word was found
     */
    public void searchForWord(String searchWord, Set<String> emails, Set<String> phone) {
        try {
            URL url = new URL(searchWord);
            if (!crawl(searchWord))
                throw new MalformedURLException("ERROR! Call crawl() before performing analysis on the document");
            if (this.htmlDocument == null) {
                throw new MalformedURLException("ERROR! Call crawl() before performing analysis on the document");
            }
            String input = this.htmlDocument.toString();
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();


//                String pageSource = IOUtils.toString(connection.getInputStream(), "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements emailLinks = htmlDocument.select("a[href^=mailto:]");

            for (Element emailLink : emailLinks) {
                String sourceemail = emailLink.attr("href").substring(7);  // remove the "mailto:" part
                String email = sourceemail.split("\\?")[0]; // remove the subject part
                emails.add(email);
            }
            Pattern pattern =
                    Pattern.compile("[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}", Pattern.CASE_INSENSITIVE);
            Matcher matchs = pattern.matcher(input);
            Pattern pattern1 = Pattern.compile("(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}", Pattern.CASE_INSENSITIVE);
            Matcher matchs1 = pattern1.matcher(input);
            while (matchs.find()) {
                emails.add(matchs.group());
            }
            while (matchs1.find()) {
                phone.add(matchs1.group());
            }
        } catch (MalformedURLException e) {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            e.printStackTrace();
        }
    }

    public List<String> getLinks() {
        return this.links;
    }

}
