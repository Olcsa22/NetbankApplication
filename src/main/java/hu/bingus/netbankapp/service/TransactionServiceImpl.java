package hu.bingus.netbankapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bingus.netbankapp.exceptions.EntityNotFoundException;
import hu.bingus.netbankapp.exceptions.UnaccessibleByUserException;
import hu.bingus.netbankapp.model.Account;
import hu.bingus.netbankapp.model.Transaction;
import hu.bingus.netbankapp.model.User;
import hu.bingus.netbankapp.util.AbstractCriteriaService;
import hu.bingus.netbankapp.util.ContextProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.Calendar;

@Service("TransactionService")
@Transactional
public class TransactionServiceImpl extends AbstractCriteriaService<Transaction> implements TransactionService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public TransactionServiceImpl() {
        super(Transaction.class);
    }

    public Boolean createTransaction(Transaction transaction) throws EntityNotFoundException, UnaccessibleByUserException {
        Account sourceAccount = ContextProvider.getBean(AccountServiceImpl.class).findById(transaction.getSourceAccountId());
        Account targetAccount = ContextProvider.getBean(AccountServiceImpl.class).findById(transaction.getTargetAccountId());
        if(sourceAccount != null && targetAccount != null){
            if(sourceAccount.getBalance()>=transaction.getAmount()){
                sourceAccount.setBalance(sourceAccount.getBalance()-transaction.getAmount());
                targetAccount.setBalance(targetAccount.getBalance()+ transaction.getAmount());
                ContextProvider.getBean(AccountServiceImpl.class).getCurrentSession().update(sourceAccount);
                ContextProvider.getBean(AccountServiceImpl.class).getCurrentSession().update(targetAccount);
                transaction.setDeleted(false);
                super.getCurrentSession().saveOrUpdate(transaction);
                return Boolean.TRUE;
            }else{
                throw new UnaccessibleByUserException("Hiba a tranzakci?? v??grehajt??sa k??zben. Nincs el??g fedezet");
            }
        }else{
            throw new EntityNotFoundException("Hiba a tranzakci?? v??grehajt??sa k??zben. Hib??s fi??k megadva");
        }
    }

    public Boolean stornoTransaction(Long id) throws EntityNotFoundException, UnaccessibleByUserException {
        Transaction transaction = super.findById(id);
        if(transaction!=null){

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR,-24*30);
            long thirtyDaysMinus = calendar.getTimeInMillis();

            if(transaction.getTransactionTime().after(new Timestamp(thirtyDaysMinus))){
                super.getCurrentSession().update(transaction);
                Account sourceAccount = ContextProvider.getBean(AccountServiceImpl.class).findById(transaction.getSourceAccountId());
                Account targetAccount = ContextProvider.getBean(AccountServiceImpl.class).findById(transaction.getTargetAccountId());
                sourceAccount.setBalance(sourceAccount.getBalance()+transaction.getAmount());
                targetAccount.setBalance(targetAccount.getBalance()-transaction.getAmount());
                transaction.setDeleted(true);
                super.getCurrentSession().saveOrUpdate(transaction);
                ContextProvider.getBean(AccountServiceImpl.class).getCurrentSession().update(sourceAccount);
                ContextProvider.getBean(AccountServiceImpl.class).getCurrentSession().update(targetAccount);
                return Boolean.TRUE;
            }else{
                throw new UnaccessibleByUserException("A tranzakci?? 30 napon t??l esik, nem sztorn??zhat??.");
            }
        }else{
            throw new EntityNotFoundException("Hiba a tranzakci?? sztorn??z??sakor. Nem l??tez?? tranzakci??.");
        }
    }

    //A swagger k??ri a nevet, de a sima principal miatt mindegy mit ??runk be, a bejelentkezett usert fogja n??zni.
    public Boolean stornoTransactionClient(Long id, Principal principal) throws EntityNotFoundException, UnaccessibleByUserException {
        Transaction transaction = super.findById(id);
        if(transaction!=null){
            Account account = ContextProvider.getBean(AccountServiceImpl.class).findById(transaction.getSourceAccountId());
            User user = ContextProvider.getBean(UserServiceImpl.class).findByUsername(principal.getName());
            if(user.getId() != account.getUserId()){
                throw new UnaccessibleByUserException("Nem a felhaszn??l??h??z tartoz?? tranzakci??");
            }else{
                if(!transaction.getDeleted()) {
                    return stornoTransaction(id);
                }else{
                    throw new UnaccessibleByUserException("A tranzakci?? m??r sztorn??zva van.");
                }
            }
        }else{
            throw new EntityNotFoundException("Nem tal??lhat?? tranzakci??.");
        }

    }

}
