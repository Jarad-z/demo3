package com.example.demo3;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Contact {
    private final StringProperty name;
    private final StringProperty phone;
    private final StringProperty email;
    private final StringProperty group;

    public Contact(String name, String phone, String email, String group) {
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
        this.group = new SimpleStringProperty(group);
    }

    // Getter 和 Setter
    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }
    public StringProperty nameProperty() { return name; }

    public String getPhone() { return phone.get(); }
    public void setPhone(String phone) { this.phone.set(phone); }
    public StringProperty phoneProperty() { return phone; }

    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public StringProperty emailProperty() { return email; }

    public String getGroup() { return group.get(); }
    public void setGroup(String group) { this.group.set(group); }
    public StringProperty groupProperty() { return group; }
}
