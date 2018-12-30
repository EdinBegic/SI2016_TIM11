package com.timxyz.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

@Entity
public class Account extends BaseModel {
    private String fullName;
    private String email;
    private String username;
    private String password;
    private Collection<AccessLog> accessLogs;
    private Role role;
    private Collection<Audit> audits;

    public Account(String fullName, String email, String username, String password, Role role) {
        this.fullName = fullName;
        this.email = email;
        this.username = username;
        this.role = role;
        this.setRawPassword(password);
    }

    public Account(String fullName, String email, String username, String password, Role role, boolean seed) {
        this.fullName = fullName;
        this.email = email;
        this.username = username;
        this.role = role;
        this.setPassword(password);
    }

    public Account() {
    }

    @Basic
    @Column(name = "fullName", nullable = false)
    @Size(min = 4, max = 255) @NotNull
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Basic
    @Column(name = "email", unique = true, nullable = false)
    @Email
    @Size(max = 255) @NotNull
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "username", unique = true, nullable = false)
    @Size(min = 4, max = 16) @NotNull
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password", nullable = false)
    @Size(min = 8, max = 255) @NotNull
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    public Collection<AccessLog> getAccessLogs() {
        return accessLogs;
    }

    public void setAccessLogs(Collection<AccessLog> accessLogs) {
        this.accessLogs = accessLogs;
    }

    @ManyToOne
    @JoinColumn(name = "roleId", referencedColumnName = "id", nullable = false)
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    public Collection<Audit> getAudits() {
        return audits;
    }

    public void setAudits(Collection<Audit> audits) {
        this.audits = audits;
    }

    public void setRawPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String result = encoder.encode(password);
        this.password = result;
    }

}
