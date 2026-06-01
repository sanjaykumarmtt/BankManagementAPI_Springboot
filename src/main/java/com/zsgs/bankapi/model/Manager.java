package com.zsgs.bankapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Manager")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(name = "age")
    private int dob;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "mobileNumber", nullable = false)
    private String mobileNo;
    
    private String role="MANAGER";

    public Manager() {}

    public Manager(String name, String password, int dob, String email, String mobileNo) {
        this.name = name;
        this.password = password;
        this.dob = dob;
        this.email = email;
        this.mobileNo = mobileNo;
    }
    
    public String getRole() {
		return role;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDob() {
        return dob;
    }

    public void setDob(int dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
