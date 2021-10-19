package FileProcessor;

public class FileInvalidException extends Exception{

    public FileInvalidException(){
        super("An invalid file has been encountered. File will be skipped");
    }
    public FileInvalidException(String s){
        super(s);
    }
    public String getMessage(){
        return super.getMessage();
    }
}
