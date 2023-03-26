package models;

import java.util.ArrayList;
import java.util.List;

public class CustomerList {

    List<Customer> customerList;

    public CustomerList(List<Customer> customerList) {
        this.customerList = new ArrayList<>();
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }
}
