package com.mykyta.crud.model;

public class Specialty {
    private Integer id;
    private String specialtyName;

    public Specialty(Integer id, String specialtyName) {
        this.id = id;
        this.specialtyName = specialtyName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }

    @Override
    public String toString() {
        return "Specialty{" +
                "id=" + id +
                ", specialtyName='" + specialtyName + '\'' +
                '}';
    }
}
