import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.io.IOException;
import java.io.FileWriter;


public class WebCrawler {
    private static final int MAX_PAGES_TO_SEARCH = 5;
     public static int countPagesVisited = 0;
    private Set<String> pagesVisited = new HashSet<String>();
    private List<String> pagesToVisit = new LinkedList<String>();
    private Set<String> emails = new HashSet<>();

    private Set<String> phone = new HashSet<>();
    public static boolean isValid(String url)
    {
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
    private String nextUrl()
    {


            String nextUrl;
            do
            {
                nextUrl = this.pagesToVisit.remove(0);

            } while(this.pagesVisited.contains(nextUrl));
            this.pagesVisited.add(nextUrl);
            return nextUrl;

    }

    public void search(String url, String searchWord)
    {
        while(this.pagesVisited.size() < MAX_PAGES_TO_SEARCH)
        {
            String currentUrl;
            SpiderLeg leg = new SpiderLeg();
            if(this.pagesToVisit.isEmpty())
            {
                currentUrl = url;
                this.pagesVisited.add(url);
            }
            else
            {
                currentUrl = this.nextUrl();
            }
            if(isValid(currentUrl) && leg.crawl(currentUrl)){
//                leg.crawl(currentUrl); // Lots of stuff happening here. Look at the crawl method in
                leg.searchForWord(currentUrl, emails,phone);
            }
            // SpiderLeg
            int i = this.pagesToVisit.size();
            this.pagesToVisit.addAll(leg.getLinks());
            this.pagesToVisit.addAll(leg.getLinks());
            countPagesVisited++;
            if(i == this.pagesToVisit.size()){
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
                i++;
                System.out.println( i + " " + x);
                crawler.search(x, "computer");
                try (FileWriter fw = new FileWriter("output.csv", true);
                     BufferedWriter bw = new BufferedWriter(fw);
                     PrintWriter out = new PrintWriter(bw)) {
                    out.println("For the website: " + x);
                    out.println("The emails from this website are: " + crawler.emails);
                    out.println("The emails from this website are: " + crawler.phone);
                    out.println();
                } catch (IOException e) {
                    //exception handling left as an exercise for the reader
                    e.printStackTrace();
                }
                if(countPagesVisited > 100) {
                    break;
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}