package FileProcessor;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class tester {
    
    
    public static void main(String[] args) {
        SimpleDateFormat simpleformat = new SimpleDateFormat("MMM");
        String strMonth= simpleformat.format(new Date());
        System.out.println("Month in MMMM format = "+strMonth);


        String greeting = "Hello";
        System.out.println(greeting.substring(0,3));
    }
    

}
