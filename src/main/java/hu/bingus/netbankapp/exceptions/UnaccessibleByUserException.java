package hu.bingus.netbankapp.exceptions;

public class UnaccessibleByUserException extends Exception {

    public UnaccessibleByUserException(String reason){
        super(reason);
    }

    public UnaccessibleByUserException(String reason, Throwable throwable){
        super(reason,throwable);
    }

}
