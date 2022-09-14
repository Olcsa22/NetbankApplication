package hu.bingus.netbankapp.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Table(name = "transactions")
@Entity
public class Transaction {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "source_account_id")
    private Long sourceAccountId;
    @Column(name = "target_account_id")
    private Long targetAccountId;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "transaction_time")
    private Timestamp transactionTime;

}
