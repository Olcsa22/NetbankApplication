package hu.bingus.netbankapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bingus.netbankapp.model.Account;
import hu.bingus.netbankapp.model.Transaction;
import hu.bingus.netbankapp.util.AbstractCriteriaService;
import hu.bingus.netbankapp.util.ContextProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.Calendar;

@Service("TransactionService")
public class TransactionServiceImpl extends AbstractCriteriaService<Transaction> implements TransactionService {

    private final ObjectMapper objectMapper;

    public TransactionServiceImpl(ObjectMapper mapper) {
        super(Transaction.class);
        this.objectMapper=mapper;
    }

    public AbstractMap.SimpleEntry<JsonNode, HttpStatus> createTransaction(Transaction transaction){
        Account sourceAccount = ContextProvider.getBean(AccountServiceImpl.class).findById(transaction.getSourceAccountId());
        Account targetAccount = ContextProvider.getBean(AccountServiceImpl.class).findById(transaction.getTargetAccountId());
        if(sourceAccount != null && targetAccount != null){
            if(sourceAccount.getBalance()>=transaction.getAmount()){
                sourceAccount.setBalance(sourceAccount.getBalance()-transaction.getAmount());
                targetAccount.setBalance(targetAccount.getBalance()+ transaction.getAmount());
                ContextProvider.getBean(AccountServiceImpl.class).getCurrentSession().update(sourceAccount);
                ContextProvider.getBean(AccountServiceImpl.class).getCurrentSession().update(targetAccount);
                super.getCurrentSession().saveOrUpdate(transaction);
                JsonNode node = objectMapper.createObjectNode().put("reason","Sikeres tranzakció végrehajtás.");
                return new AbstractMap.SimpleEntry<>(node,HttpStatus.OK);
            }else{
                JsonNode node = objectMapper.createObjectNode().put("reason","Hiba a tranzakció végrehajtása közben. Nincs elég fedezet.");
                return new AbstractMap.SimpleEntry<>(node,HttpStatus.BAD_REQUEST);
            }
        }else{
            JsonNode node = objectMapper.createObjectNode().put("reason","Hiba a tranzakció végrehajtása közben. Hibás fiókok megadva.");
            return new AbstractMap.SimpleEntry<>(node,HttpStatus.BAD_REQUEST);
        }
    }

    public AbstractMap.SimpleEntry<JsonNode, HttpStatus> stornoTransaction(Long id){
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
                targetAccount.setBalance(targetAccount.getBalance()+transaction.getAmount());
                ContextProvider.getBean(AccountServiceImpl.class).getCurrentSession().update(sourceAccount);
                ContextProvider.getBean(AccountServiceImpl.class).getCurrentSession().update(targetAccount);
                JsonNode node = objectMapper.createObjectNode().put("reason","Tranzakció sztornózva.");
                return new AbstractMap.SimpleEntry<>(node,HttpStatus.OK);
            }else{
                JsonNode node = objectMapper.createObjectNode().put("reason","A tranzakció 30 napon túl esik, nem sztornózható.");
                return new AbstractMap.SimpleEntry<>(node,HttpStatus.BAD_REQUEST);
            }
        }else{
            JsonNode node = objectMapper.createObjectNode().put("reason", "Hiba a tranzakció sztornózásakor. Nem létező tranzakció.");
            return new AbstractMap.SimpleEntry<>(node,HttpStatus.BAD_REQUEST);
        }
    }

}
