package com.evsyukoov.module.entities;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "cities")
public class City {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "region")
    private String region;

    @Column(name = "district")
    private String district;

    @Column(name = "population")
    private int population;

    @Column(name = "foundation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date foundation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public Date getFoundation() {
        return foundation;
    }

    public void setFoundation(Date foundation) {
        this.foundation = foundation;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        return "City{" +
                "name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", district='" + district + '\'' +
                ", population=" + population +
                ", foundation=" + formatter.format(foundation) +
                '}';
    }
}
