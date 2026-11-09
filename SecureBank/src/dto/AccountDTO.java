package dto;

import java.math.BigDecimal;


public class AccountDTO {
    private int id;
    private String holderName;
    private String accountNumber;
    private BigDecimal balance;

    public AccountDTO(int id, String holderName, String accountNumber, BigDecimal balance) {
        this.id = id;
        this.holderName = holderName;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public AccountDTO(String holderName, String accountNumber, BigDecimal balance) {
        this(0, holderName, accountNumber, balance);
    }

    public int getId() { return id; }
    public String getHolderName() { return holderName; }
    public String getAccountNumber() { return accountNumber; }
    public BigDecimal getBalance() { return balance; }
    public void setId(int id) { this.id = id; }
}
