package hu.bingus.netbankapp.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table(name = "transactions")
@Entity
@Data
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
