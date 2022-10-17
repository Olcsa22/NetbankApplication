package hu.bingus.netbankapp.exceptions;

public class EntityAlreadyExistsException extends Exception{

    public EntityAlreadyExistsException(String reason, Throwable throwable){
        super(reason,throwable);
    }

    public EntityAlreadyExistsException(String reason){
        super(reason);
    }
}
