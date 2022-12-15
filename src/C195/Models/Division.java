package C195.Models;

import java.sql.Timestamp;
import java.util.Date;

/**
 * division class
 */
public class Division {
    int divisionId;
    String division;
    Date createDate;
    String createdBy;
    Timestamp lastUpdate;
    String lastUpdatedBy;
    int countryId;

    /**
     * Constructor for the division class
     * @param divisionId
     * @param division
     * @param createDate
     * @param createdBy
     * @param lastUpdate
     * @param lastUpdatedBy
     * @param countryId
     */
    public Division(int divisionId, String division, Date createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy, int countryId) {
        this.divisionId = divisionId;
        this.division = division;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryId = countryId;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public String getDivision() {
        return division;
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

    public int getCountryId() {
        return countryId;
    }
}
