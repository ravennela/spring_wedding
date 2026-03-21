package com.example.online.event.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matches Flutter payload: images[].imageUrl + publicId (also url, public_id).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DecorationImageRequest {

    @JsonProperty("imageUrl")
    @JsonAlias({ "url", "image_url" })
    private String imageUrl;

    @JsonProperty("publicId")
    @JsonAlias("public_id")
    private String publicId;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }
}
