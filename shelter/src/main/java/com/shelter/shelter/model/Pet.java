package com.shelter.shelter.model;

import javax.persistence.*;

    @Entity
    @Table(name = "Pet_catalogue")
    public class Pet {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        @Column
        private String name;
        @Column
        private String breed;
        @Column
        private String description;
        @Column
        private int age;

        @ManyToOne( fetch = FetchType.EAGER)
        @JoinColumn(name = "user_id")
        private User user;

        @Column(nullable = false)
        private boolean stan;

        public boolean isStan() {
            return stan;
        }

        public void setStan(boolean stan) {
            this.stan = stan;
        }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Pet() {
    }

    public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBreed() {
            return breed;
        }

        public void setBreed(String breed) {
            this.breed = breed;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

