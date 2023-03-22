package uge.fr.ugeoverflow.dto;

public class UsersDto {
    private String ImageUrl;
    private String username;
    private String country;

    public UsersDto(String ImageUrl, String username, String country) {
        this.ImageUrl = ImageUrl;
        this.username = username;
        this.country = country;
    }

    public UsersDto(){
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
