package com.example.multipleline.reader;

import java.util.ArrayList;

import com.example.multipleline.entity.Customer;
import com.example.multipleline.entity.Transaction;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class CustomerFileReader implements ItemStreamReader<Customer>{

    private Object currentItem = null;
    private ItemStreamReader<Object> delegate;
    
    public CustomerFileReader(ItemStreamReader<Object> delegate) {
        this.delegate = delegate;
    }


    @Override
    public void close() throws ItemStreamException {
        this.delegate.close();
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.delegate.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        this.delegate.update(executionContext);
        
    }

    @Override
    public Customer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (currentItem == null) {
            currentItem = delegate.read();
        }
        Customer item = (Customer) currentItem;
        currentItem = null;
        if (item != null) {
            item.setTransactions(new ArrayList<>());
            while(peek() instanceof Transaction) {
                item.getTransactions().add((Transaction)currentItem);
                currentItem = null;
            }
        }
        return item;
    }


    private Object peek() throws Exception {
        if (currentItem == null) {
            currentItem = delegate.read();
        }
        return currentItem;
    }
}
