package com.appgreat.beerapp.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;

import java.io.Serializable;

@Table
@JsonIgnoreProperties({"first_brewed","abv","ibu","target_fg","target_og","ebc",
        "srm","ph","attenuation_level","volume","boil_volume",
        "method","ingredients","hops","food_pairing","brewers_tips","contributed_by"})
public class Beer implements Serializable {

    private Long id;
    private String name;
    private String tagline;
    private String image_url;
    private String description;
    @Ignore
    private int favorite;

    public Beer() {
    }

    public Beer(Long id, String name, String tagline, String image_url, String description, int favorite) {
        this.id = id;
        this.name = name;
        this.tagline = tagline;
        this.image_url = image_url;
        this.description = description;
        this.favorite = favorite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
}
