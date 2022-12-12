package C195.Models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Customer {
    public int customerId;
    public String name;
    public String address;
    public String postalCode;
    public String phone;
    public LocalDateTime createdDate;
    public String createdBy;
    public Timestamp lastUpdate;
    public String lastUpdatedBy;
    public int divisionId;

    public Customer (int customerId, String name, String address, String postalCode, String phone, LocalDateTime createdDate, String createdBy, Timestamp lastUpdate,String lastUpdatedBy, int divisionId){
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return name;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
