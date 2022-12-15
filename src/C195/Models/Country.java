package C195.Models;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Country class
 */
public class Country {
    int countryId;
    String country;
    Date createDate;
    String createdBy;
    Timestamp lastUpdate;
    String lastUpdatedBy;

    /**
     * Constructor for country class
     * @param countryId
     * @param country
     * @param createDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdatedBy
     */
    public Country(int countryId, String country, Date createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy) {
        this.countryId = countryId;
        this.country = country;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public int getCountryId() {
        return countryId;
    }

    public String getCountry() {
        return country;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }
}
