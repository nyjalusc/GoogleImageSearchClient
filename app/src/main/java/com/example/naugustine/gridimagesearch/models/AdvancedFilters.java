package com.example.naugustine.gridimagesearch.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.List;

// Don't need multiple filter settings. This is a single user app.
// Singleton class
@Table(name = "AdvancedFilters")
public class AdvancedFilters extends Model implements Serializable {
    @Column(name = "Size")
    private String size;
    @Column(name = "Color")
    private String color;
    @Column(name = "Type")
    private String type;
    @Column(name = "SafetyLevel")
    private String safetyLevel;
    @Column(name = "Site")
    private String site;

    public AdvancedFilters() {
        super();
    }

    // Crate a row with default values when the app is launced for the first time
    // Keep on reusing the same row for saving future changes in filter settings
    public void init() {
        List<AdvancedFilters> result = readFromDb();
        if (result.size() == 0)
        {
            this.size = "Any";
            this.color = "Any";
            this.type = "Any";
            this.safetyLevel = "Moderate";
            this.site = "";
            this.save();
        } else {
            setSize(result.get(0).getSize());
            setColor(result.get(0).getColor());
            setType(result.get(0).getType());
            setSafetyLevel(result.get(0).getSafetyLevel());
            setSite(result.get(0).getSite());
        }
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSafetyLevel() {
        return safetyLevel;
    }

    public void setSafetyLevel(String safetyLevel) {
        this.safetyLevel = safetyLevel;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public List<AdvancedFilters> readFromDb() {
        return new Select().from(AdvancedFilters.class).execute();
    }
}
