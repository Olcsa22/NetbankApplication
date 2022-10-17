package hu.bingus.netbankapp.exceptions;

public class EntityNotFoundException extends Exception{

    public EntityNotFoundException(String reason){
        super(reason);
    }

    public EntityNotFoundException(String reason, Throwable throwable){
        super(reason, throwable);
    }

}
