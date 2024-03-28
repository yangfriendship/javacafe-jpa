package me.youzheng.javacafejpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class JavacafejpaMain {

    public static void main(String[] args) {
        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

    }

}
