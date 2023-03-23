package uge.fr.ugeoverflow.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uge.fr.ugeoverflow.security.UserRole;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(nullable = false)
    @NotBlank(message = "firstname is required !")
    @Size(min = 2, max = 20, message = "firstname must be between 2 and 20 characters")
    private String firstname;

    @Column(nullable = false)
    @NotBlank(message = "lastname is required !")
    @Size(min = 2, max = 20, message = "lastname must be between 2 and 20 characters")
    private String lastname;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "username is required !")
    @Size(min = 3, max = 20, message = "username must be between 3 and 20 characters")
    private String username;

    @Column(unique = true)
    @Email(message = "Please provide a valid email address")
    private String email;
    @Column(nullable = false)
    @NotBlank(message = "password is required !")
    @Size(min = 3, message = "password must be at least 6 characters")//TODO : change to 6 later
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must contain at least one digit, one lower case, one upper case, one special character and no whitespace")
    private String password;

    @Size(min = 0, max = 10000, message = "bio must be less than " +
            "10000 characters")
    private String bio;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(nullable = false)
    private String role;

    @Column
    private String imageUrl="";

    @CreationTimestamp
    @Column(name = "creation_time", nullable = false, updatable = false)
    private LocalDateTime creationTime;

//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "follow_relationship",
//            joinColumns = @JoinColumn(name = "follower_id"),
//            inverseJoinColumns = @JoinColumn(name = "followed_id")
//    )
//    private Set<User> followers = new HashSet<>();

//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "follow_rel",
//            joinColumns = @JoinColumn(name = "follower_id"),
//            inverseJoinColumns = @JoinColumn(name = "followed_id")
//    )
//    private Set<FollowRelationship> followers = new HashSet<>();

//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "follow_relationship",
//            joinColumns = @JoinColumn(name = "follower_id"),
//            inverseJoinColumns = @JoinColumn(name = "followed_id")
//    )
//    private Set<FollowRelationship> followers = new HashSet<>();
//
//    @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL)
//    private Set<FollowRelationship> following = new HashSet<>();

    @Column(length = 10000)
    private String token;

    public User(String firstName, String lastname, String username, String email, String password) {
        this.firstname = firstName;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


    public User() {
    }


    // Getters and setters

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return UserRole.valueOf(role).getAuthorities();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
    //hashcode , toString and equals

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstname + '\'' +
                ", lastName='" + lastname + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", address=" + address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    //
//
//    public Set<FollowRelationship> getFollowers() {
//        return followers;
//    }
//
//    public void follow(User user) {
//
//        followers.add(new FollowRelationship(this, user));
//
//    }
//
//    public void unfollow(User user) {
//        followers.remove(user);
//    }
//
//    public boolean isFollowing(User user) {
//        return followers.contains(user);
//    }
//
//    public void setFollowers(Set<FollowRelationship> followers) {
//        this.followers = followers;
//    }

//    public Set<FollowRelationship> getFollowing() {
//        return following;
//    }
//
//    public void setFollowing(Set<FollowRelationship> following) {
//        this.following = following;
//    }

    // UserDetails methods implementation
    // We don't need these functionalities for our application may be in the future
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Set<User> getFollowedUsers() {
        return null;
    }
}