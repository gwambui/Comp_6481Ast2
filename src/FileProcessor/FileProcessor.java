package FileProcessor;

import com.sun.xml.internal.bind.v2.TODO;

import java.io.*;
import java.util.Scanner;

//import java.io.PrintWriter;
//import java.io.FileOutputStream;
//import java.io.FileNotFoundException;
//import java.io.FileInputStream;

public class FileProcessor {
    static int outfileindex = 0;
    public static void  processFilesForValidation(Scanner[] scanners, PrintWriter[] pwieee, PrintWriter[] pwacm, PrintWriter[] pwnj, String[] infiles, File[] outfiles){
        String line = null;
        String fullfile;
        int i=0;
        OutputGenerator og;
        for( Scanner sc :scanners) {
            fullfile ="";
            try{
                while (sc.hasNextLine()) {
                    line = sc.nextLine();
    //                System.out.println(line);

                    if (line.contains("{}")) {
                       throw new FileInvalidException();

                    } else {
                        fullfile = fullfile + line;
                    }
                }
                og = new OutputGenerator(fullfile, pwieee[i], pwacm[i],  pwnj[i]);

            }catch(FileInvalidException e){
                System.out.println(e.getMessage());
//                    link to the input and output file name to report invalid file and delete outfiles
                System.out.println("Invalid file: "+infiles[i] +" Invalid line:"+ line);
                cleanOutfiles(infiles[i],pwieee[i], pwacm[i],  pwnj[i]);
                fullfile="";
            }
            i++;
        }
        closeOutfiles(pwieee, pwacm,  pwnj);
        closeInFiles(scanners);
    }
    public static void main(String[] args) {
//  calling close outfiles before running program to clean outputfolder
        cleanOutfiles();


        File f = new File("Comp6481_F21_Assg2_Files");
        String[] files = f.list();
        String[] inputFiles= new String[files.length];
        int i = 0;
        Scanner[] scanners= new Scanner[files.length];
        PrintWriter pwieee[] = new PrintWriter[files.length];
        PrintWriter pwacm[] = new PrintWriter[files.length];
        PrintWriter pwnj[] = new PrintWriter[files.length];
        File outFiles[] = new File[3*files.length];


        try{
            for ( String name : files){
               if (name.endsWith(".bib")  && files[i].matches(".*\\d.*")){
                   inputFiles[i] = name;
//                 Open input files. If any is missing. close all and terminate process
                   File filename = new File("Comp6481_F21_Assg2_Files/"+name);
                   if (!filename.exists()){
                       System.out.println("file"+filename+" is missing. Program will terminate and close all input files");
                       closeInFiles(scanners);
                   }
                   scanners[i] = new Scanner(new FileInputStream(filename));
//                  create output files extract the file number ## from the Latex##.bib files
                  String filenumber ;
                   if(name.substring(5,7).matches("\\d+")){
                      filenumber = name.substring(5,7);
                   }else if(name.substring(5,6).matches("\\d+"))
                   {filenumber = name.substring(5,6);
                   }else{
                       filenumber = name;
                   }


                   File f1 = new File("outfiles/IEEE"+filenumber+".json");
                   File f2 = new File("outfiles/ACM"+filenumber+".json");
                   File f3 = new File("outfiles/NJ"+filenumber+".json");

                   if(f1.exists() ){
                       System.out.println("Error: There is an existing file called: IEEE"+filenumber+".json"  + ".");
                       closeOutfiles(pwieee,pwacm,pwnj);
                       cleanOutfiles();
                   }else if (f2.exists()){
                       System.out.println("Error: There is an existing file called: ACM"+filenumber+".json" + ".");
                       closeOutfiles(pwieee,pwacm,pwnj);
                       cleanOutfiles();
                   }else if(f3.exists()){
                       System.out.println("Error: There is an existing file called: NJ"+filenumber+".json" + ".");
                       closeOutfiles(pwieee,pwacm,pwnj);
                       cleanOutfiles();
                   }
                   pwieee[i] = new PrintWriter(new FileOutputStream(f1,true));
                   pwacm[i] = new PrintWriter(new FileOutputStream(f2,true));
                   pwnj[i] = new PrintWriter(new FileOutputStream(f3,true));
                   saveOutfile(outFiles,f1,f2,f3);
                   i++;

               }

            }

        }catch (FileNotFoundException fe){
            System.out.println("Could not open input file "+inputFiles[i] +" for reading."
                    + " Please check if file exists.");
            System.out.print("Program will terminate after closing any opened files.");
            closeInFiles(scanners);
            System.exit(0);

        }
        processFilesForValidation(scanners,pwieee,pwacm,pwnj,inputFiles,outFiles);

//        User can request to read the output files *using buffered reader
        System.out.println("Enter output name of the file you wish to read:");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader("outfiles/"+name));
            displayOutfile(br);
        } catch (FileNotFoundException e) {
            System.out.println("The file could not be found, please try again");
            name = sc.nextLine();
            try {
                br = new BufferedReader(new FileReader("outfiles/"+name));

                displayOutfile(br);

            } catch (FileNotFoundException ex) {
                System.out.println("File could not be found. exiting program");
                System.exit(0);
            } catch (IOException Ie) {
                System.out.println("Error occured while reading the output file");
                System.out.println(Ie.getMessage());
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void displayOutfile(BufferedReader br) throws IOException {
        String content;
        content = br.readLine();
        while (content != null){
            System.out.println(content);
            content = br.readLine();
        }
        br.close();

    }

    private static void cleanOutfiles() {

        File f = new File("outfiles");
        String[] files = f.list();
        for (String of : files) {
            File file = new File(f.getPath(),of);
            file.delete();
        }
    }

    private static void cleanOutfiles(String infile,PrintWriter pw1,PrintWriter pw2, PrintWriter pw3){
        pw1.close();
        pw2.close();
        pw3.close();
        String filenumber = "";
        if(infile.substring(5,7).matches("\\d+")){
            filenumber = infile.substring(5,7);
        }else if(infile.substring(5,6).matches("\\d+")) {
            filenumber = infile.substring(5, 6);
        }
        File f = new File("outfiles");
        String[] files = f.list();
        for (String of : files) {
            if (of.contains(filenumber)){
                File file = new File(f.getPath(),of);

                System.out.println("delete outfiles for invalid infile "+ file.getPath()+": "+file.delete());
            }

        }

    }

    private static void saveOutfile(File[] outFiles, File f1, File f2, File f3) {
        outFiles[outfileindex++] = f1;
        outFiles[outfileindex++] = f2;
        outFiles[outfileindex++] = f3;
    }

    private static void closeInFiles(Scanner[] scanners) {
        for(Scanner sc : scanners){
            sc.close();
        }
    }

    private static void closeOutfiles(PrintWriter[] pwieee, PrintWriter[] pwacm, PrintWriter[] pwnj) {
        if (pwieee.length > 0) {
            for (PrintWriter pw : pwieee) {
                pw.close();
            }
        }
        if (pwacm.length > 0) {
            for (PrintWriter pw : pwacm) {
                pw.close();
            }
        }
        if (pwnj.length > 0) {
            for (PrintWriter pw : pwnj) {
                pw.close();
            }
        }
    }
}
