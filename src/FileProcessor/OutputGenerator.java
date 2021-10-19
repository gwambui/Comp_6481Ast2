package FileProcessor;

import java.io.PrintWriter;
import java.util.*;

public class OutputGenerator {
    private String data;
    private PrintWriter ieeepw;
    private PrintWriter acmpw;
    private PrintWriter njpw;
    private StringTokenizer st;

    private HashMap<String, HashMap<String, String>> datahash = new HashMap<String, HashMap<String, String>>();


    public OutputGenerator() {

    }

    public OutputGenerator(String data, PrintWriter ieee, PrintWriter acm, PrintWriter nj) {
        data = data;
        ieeepw = ieee;
        acmpw = acm;
        njpw = nj;
        st = new StringTokenizer(data, "@");
        datahash = makehash();
        printieee();
        printacm();
        printnj();

    }

    private void printnj() {
//        1.Author Surname, Author Initial. Title. Publication Title Volume number, Pages Used (Year Published).
//        J. Park & J. N. James & Q. Li & Y. Xu & W. Huang. Optimal DASH-Multicasting over LTE. IEEE Transactions on Vehicular Technology. PP, 15-27(2018).
        Iterator<Map.Entry<String, HashMap<String, String>>> itr = datahash.entrySet().iterator();
        int i = 1;
        String report;
        Map.Entry<String, HashMap<String, String>> entry = null;
        while ((itr).hasNext()) {
            report = "";
            entry = itr.next();
            String author;
            if (entry.getValue().get("author").split("and").length > 1) {
                author = entry.getValue().get("author").split("and")[0] + "et al";
            } else {
                author = entry.getValue().get("author");
            }
            report += entry.getValue().get("author").replace("and","&") + ". "
                    + entry.getValue().get("title") + ". "+ entry.getValue().get("journal") + ". " + entry.getValue().get("volume") + ", "
                    + entry.getValue().get("pages")+ " (" + entry.getValue().get("year") + "). ";

//            System.out.println(report);
            njpw.println(report);
            i++;

        }
    }

    private void printacm() {
        String report;
//        [1] Author. year. title. journal, volume number, pagerange. DOI:
//[4]	T. K. Roman et al. 2018.  IP-Based Mobility Optimization. Mobile Networks and Applications. AA, 4 (2018), 64-82. DOI:https://doi.org/233.5490/TPS.2018.8700003.
//   [1] J. Park et al. 2018. Optimal DASH-Multicasting over LTE. IEEE Transactions on Vehicular Technology. PP, 99 (2018), 15-27. DOI:https://doi.org/10.1109/TVT.2018.2789899.
        Iterator<Map.Entry<String, HashMap<String, String>>> itr = datahash.entrySet().iterator();
        int i = 1;
        String author;
        Map.Entry<String, HashMap<String, String>> entry = null;
        while((itr).hasNext()) {
            report = "";
            entry = itr.next();

            if(entry.getValue().get("author")  .split("and") .length > 1){
                author = entry.getValue().get("author")  .split("and")[0] +"et al";
            }else {
                author = entry.getValue().get("author");
            }
                report += " [" + i + "] " + author+". " + entry.getValue().get("year") + ". " + entry.getValue().get("title") + ". "
                    + entry.getValue().get("journal") + ". " + entry.getValue().get("volume") + ", "
                    + entry.getValue().get("number") + " ("+entry.getValue().get("year") +") ,  " + entry.getValue().get("pages")
                    + ". DOI:https://doi.org/" + entry.getValue().get("doi");

//            System.out.println(report);
            acmpw.println(report);
            i++;

        }

    }

    private void printieee() {

        String report;
//        [#]      Author, “Title,” Journal, volume, number, page range, month year, DOI.
//        T. K. Roman, C. Henry Jr., L. Fevens. " IP-Based Mobility Optimization", Mobile Networks and Applications, vol. AA, no. 4, p. 64-82, February 2018.
//J. Park, J. N. James, Q. Li, Y. Xu, W. Huang. "Optimal DASH-Multicasting over LTE", IEEE Transactions on Vehicular Technology, vol. PP, no. 99, p. 15-27, January 2018.


        Iterator<Map.Entry<String, HashMap<String, String>>> itr = datahash.entrySet().iterator();

        Map.Entry<String, HashMap<String, String>> entry = null;
        while((itr).hasNext()){
            report="";
            entry = itr.next();
            report += entry.getValue().get("author")+ ", \""+entry.getValue().get("title")+"\", "
                    +entry.getValue().get("journal")+", vol. "+entry.getValue().get("volume")+", no. "
                    +entry.getValue().get("number")+", p. "+entry.getValue().get("pages")+", "
                    +entry.getValue().get("month")+". "+entry.getValue().get("year")+", doi: "+entry.getValue().get("doi");

//            System.out.println( report );
            ieeepw.println(report);
        }

    }

    private HashMap<String, HashMap<String, String>> makehash() {
        String articleId = new String();
        String tock;
        StringTokenizer token;
        StringTokenizer keyval;
        HashMap<String, String> elements ;
//        System.out.println("Number of tockens in this dict "+st.countTokens());
        while (st.hasMoreTokens()) {
            elements = new HashMap<String, String>();
            token = new StringTokenizer(st.nextToken());

            while (token.hasMoreTokens()) {
                tock = token.nextToken(",");

                if (!tock.contains("=") && tock.length() > 1) {

                    articleId = tock.replace("ARTICLE{", "").replace("}", "");

                } else if (tock.contains("=")) {
                    keyval = new StringTokenizer(tock, "=");
                    elements.put(keyval.nextToken().trim(), keyval.nextToken().replace("}", "").replace("{", "").trim());

                }
            }
            datahash.put(articleId, elements);
        }
        return datahash;
    }

}



//            System.out.println("ArticleID ++++++++++++"+articleId);

//            Iterator<Map.Entry<String, String>> itr = elements.entrySet().iterator();
//
//            Map.Entry<String,  String> entry = null;
//            while((itr).hasNext()){
//
//                entry = itr.next();
//                if( entry.getKey().equals("ISSN")){
//                    System.out.println(" found ISSN +++++++++++");
//                }
//                System.out.println( entry.getKey() + "->" + entry.getValue() );
//            }


//            System.out.println(elements);
//            System.out.println(elements.get("ISSN"));

//

//        Iterator<Map.Entry<String, HashMap<String, String>>> itr = datahash.entrySet().iterator();
//
//        Map.Entry<String, HashMap<String, String>> entry = null;
//        while((itr).hasNext()){
//
//            entry = itr.next();
//            System.out.println( entry.getKey() + "->" + entry.getValue() );
//        }
//        System.out.println(datahash);






