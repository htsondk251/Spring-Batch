package com.example.delimitedcustom.utils;

import com.example.delimitedcustom.entity.Transaction;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class TransactionFieldSetMapper implements FieldSetMapper<Transaction> {

    @Override
    public Transaction mapFieldSet(FieldSet fieldSet) throws BindException {
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(fieldSet.readString("accountNumber"));
        transaction.setTransactionDate(fieldSet.readDate("transactionDate", "yyyy-MM-dd HH:mm:ss"));
        transaction.setAmount(fieldSet.readDouble("amount"));

        return transaction;
    }
}
