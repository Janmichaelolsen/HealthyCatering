package logikk;

public class User {

    private String username;
    private String password;
    private String repeatPass;
    private String address;
    private int postnumber;
    private String city;
    private String firstName;
    private String surname;
    private String email;
    private String phone;
    public String role;

    public User(String username, String password, String firstname, String surname,
            String address, String mobilenr, int postalcode) {
        this.username = username;
        this.password = password;
        this.firstName = firstname;
        this.surname = surname;
        this.address = address;
        this.phone = mobilenr;
        this.postnumber = postalcode;
    }

    public User() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPass() {
        return repeatPass;
    }

    public void setRepeatPass(String repeatPass) {
        this.repeatPass = repeatPass;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPostnumber() {
        return postnumber;
    }

    public void setPostnumber(int postnumber) {
        this.postnumber = postnumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String toString() {
        return "username: " + username + ", password: " + password + ", Address: " + address + ", City: " + city + ", postNumber: " + postnumber + ", first name: " + firstName + ", surname: " + surname + ", email: " + email + ", phone: " + phone;
    }
}