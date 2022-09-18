package hu.bingus.netbankapp.service;

import hu.bingus.netbankapp.model.Account;
import hu.bingus.netbankapp.util.AbstractCriteriaService;

public class AccountServiceImpl extends AbstractCriteriaService<Account> implements AccountService {

    public AccountServiceImpl() {
        super(Account.class);
    }

    @Override
    public void addAccount(Account account) {
        super.getCurrentSession().saveOrUpdate(account);
    }
}
