package hu.bingus.netbankapp.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "balance")
    private Long balance;

    @Column(name = "userId")
    private Long userId;
}
