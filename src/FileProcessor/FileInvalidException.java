package FileProcessor;
// -----------------------------------------------------
// Assignment (2)
// © Pargol,Wambui
// Written by: (Pargol Poshtareh 26428126,Wambui Josphine Kinyanjui 24600878)
// -----------------------------------------------------
/**
 * FileInvalidException extends the Exception class. It is thrown when an invalid entry is
 * found in the input files.
 *
 *
 *
 */
public class FileInvalidException extends Exception{
    /**
     * Default constructor that calls the parent class constructor with a default message.
     */
    public FileInvalidException(){
        super("An invalid file has been encountered. File will be skipped");
    }
    /**
     * Parameterized constructor that accepts a string message and calls parent class constructor
     * with the message as the parameter
     * @param s String to be set to Exception message
     */
    public FileInvalidException(String s){
        super(s);
    }
    /**
     * Get message method to return the exception message contained in the exception object
     *
     */
    public String getMessage(){
        return super.getMessage();
    }
}
