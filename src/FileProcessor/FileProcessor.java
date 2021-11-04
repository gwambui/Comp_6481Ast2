package FileProcessor;
// -----------------------------------------------------
// Assignment (2)
// © Pargol,Wambui
// Written by: (Pargol Poshtareh 26428126,Wambui Josphine Kinyanjui 24600878)
// -----------------------------------------------------
/**
 * Processes .bib files and generates output files for three reference formats
 * (IEEE,ACM, Nature Journal
 * Allows the user to view output files once generated.
 *
 */
import com.sun.xml.internal.bind.v2.TODO;

import java.io.*;
import java.util.Scanner;



class FileProcessor {
    static int outfileindex = 0;
    /**
     * This method is the main engine of the operation: reads all the input files
     * Creates an OutputGenerator object and passes  the read string per file
     * If any of the input files are invalid, a FileInvalidException is thrown,
     * the file is skipped and the corresponding output files are deleted.
     * The process is completed when all the valid input files are
     * read and their outputs files written.
     * The method closes all the reader and writer objects before returning.
     * <p>
     *
     * @param  scanners  An array of scanner objects for each input file
     * @param  pwieee   An array of PrintWriter objects for each of the IEEE output files
     * @param  pwacm    An array of PrintWriter objects for each of the ACM output files
     * @param  pwnj    An array of PrintWriter objects for each of the NJ output files
     * @param  infiles an array of strings representing names of the input files
     * @param outfiles An array of all the output files created in main
     */
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
                og.printFiles();
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

    /**
     * cleanOutfiles(); is called first to clean out any output files from the previous execution
     * The main method opens all the input files and creates.
     * It first confirms that each file exists. If one file is missing,
     * it throws an fileNotFound exception and exits. If all the files are present,
     * it creates an array of scanner objects one for each input file.
     * It then creates output files for each of the three referencing formats (mentioned above)
     * If any of the output files exists, a message is printed and the program terminates
     * after deleting all output files and closing the input files.
     * If all the output files are created safely, 3 arrays array of PrintWriter objects are created
     * A list of all the output files is maintained for easy access to the file names.
     * The main engine (processFilesForValidation) is called and passed the scanner array and the
     * three PrintWriter arrays.
     *
     * @param args main method args
     */
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


                    String existingFile = "";
                   if(f1.exists() ){ existingFile = "IEEE"+filenumber+".json" + ".";

                   }else if (f2.exists()){ existingFile = "ACM"+filenumber+".json" + ".";

                   }else if(f3.exists()){ existingFile = "NJ"+filenumber+".json" + ".";

                   }
                   if(!existingFile.isEmpty()){
                       System.out.println("Error: There is an existing file called:"  + existingFile);
                       closeOutfiles(pwieee,pwacm,pwnj);
                       cleanOutfiles();
                       closeInFiles(scanners);
                       System.exit(0);
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
        System.out.println("\nEnter name of the output file you wish to read:");
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
                System.out.println("Error occurred while reading the output file");
                System.out.println(Ie.getMessage());
                System.exit(0);
            }
        } catch (IOException e) {
            System.out.println("Error occurred while reading the output file");
            System.out.println(e.getMessage());
        }


    }
/**
 *  displayOutfile is called to read one output file from the generated list of files
 *  @param br BufferedReader object for one of the output files requested by user.
 *
 */
    private static void displayOutfile(BufferedReader br) throws IOException {
        String content;
        content = br.readLine();
        while (content != null){
            System.out.println(content);
            content = br.readLine();
        }
        br.close();

    }
/**
 *  cleanOutfiles is called delete any files in the output folder.
 *
 */
    private static void cleanOutfiles() {

        File f = new File("outfiles");
        String[] files = f.list();
        for (String of : files) {
            File file = new File(f.getPath(),of);
            file.delete();
        }
    }
/**
 *  cleanOutfiles is overloaded and called with parameters to delete specific output files
 * @param infile The invalid input file that needs to be skipped
 * @param pw1 PrintWriter object for an invalid output file
 * @param pw2 PrintWriter object for an invalid output file
 * @param pw3 PrintWriter object for an invalid output file
 *
 */

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
    /**
     * saveOutfile is called to save the created output files in an array to track the output files available.
     * @param outFiles An array created to store output files size = inputfiles*3
     * @param f1 File object for one output file
     * @param f2 File object for one output file
     * @param f3 File object for one output file
     *
     */
    private static void saveOutfile(File[] outFiles, File f1, File f2, File f3) {
        outFiles[outfileindex++] = f1;
        outFiles[outfileindex++] = f2;
        outFiles[outfileindex++] = f3;
    }
/**
 *  closeInFiles is called delete any initialized scanner objects.
 * @param scanners an array of scanners initialized to read input files
 */
    private static void closeInFiles(Scanner[] scanners) {
        for(Scanner sc : scanners){
            if (sc instanceof Scanner) {
                sc.close();
            }
        }
    }
/**
 * cleanOutfiles is overloaded and called with parameters to delete all output files in the PrintWriter Arrays
 * @param pwieee PrintWriter array of objects for all initialized output files for IEEE reference format
 * @param pwacm PrintWriter array of objects for all initialized output files for ACM reference format
 * @param pwnj PrintWriter array of objects for all initialized output files for NJ reference format
 *
 */
    private static void closeOutfiles(PrintWriter[] pwieee, PrintWriter[] pwacm, PrintWriter[] pwnj) {

        for (PrintWriter pw : pwieee) {
            if(pw instanceof PrintWriter){
                pw.close();
            }

        }
        for (PrintWriter pw : pwacm) {
            if(pw instanceof PrintWriter){
                pw.close();
            }
        }
        for (PrintWriter pw : pwnj) {
            if(pw instanceof PrintWriter){
                pw.close();
            }
        }
        }

}
