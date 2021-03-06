package com.haulmont.testtask.data.entity;

import javax.persistence.*;

@Entity
@Table(name = "mechanic")
@NamedQueries({
        @NamedQuery(name = "Mechanic.findAll", query = "select m from Mechanic m"),
        @NamedQuery(name = "Mechanic.findById", query = "select m from Mechanic m where id = :id")
})
public class Mechanic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "taxes")
    private Integer taxes;

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Integer getTaxes() {
        return taxes;
    }

    public void setTaxes(Integer number) {
        this.taxes = number;
    }

    @Override
    public String toString() {
        return id + " " + firstName + " " + secondName + " " + patronymic;
    }
}
