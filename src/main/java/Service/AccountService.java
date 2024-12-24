package Service;

import java.util.List;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    
    private AccountDAO accountDAO;
    
    public AccountService(){
        accountDAO = new AccountDAO();
    }
    public List<Account> getAllAccounts() {
        return this.accountDAO.getAllAccounts();
    }
    
    public Account addAccount(Account account) {
        return this.accountDAO.insertAccount(account);
    }

    public Account loginAccount(Account account) {
        return this.accountDAO.getAccountByUserPwd(account);
    }
    
}
