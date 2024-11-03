public class Person {

    // private variables
    private String firstName;
    private String surname;
    private String team;

    // constructor:
    Person(String firstName, String surname, String team) {
        this.firstName = firstName;
        this.surname = surname;
        this.team = team;
    }

    // getters and setters:
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

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

}