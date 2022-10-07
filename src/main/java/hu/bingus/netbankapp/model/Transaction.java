package hu.bingus.netbankapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "transactions")
@Entity
@Data
public class Transaction {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_account_id")
    private Long sourceAccountId;

    @Column(name = "target_account_id")
    private Long targetAccountId;

    @Column(name = "amount")
    private Long amount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "transaction_time")
    private Timestamp transactionTime;

    @Column(name = "deleted")
    private Boolean deleted;
}
