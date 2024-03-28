package me.youzheng.javacafejpa.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    static EntityManagerFactory EMF;
    EntityManager em;

    Member member;

    @BeforeAll
    static void beforeAll() {
        EMF = Persistence.createEntityManagerFactory("default");
    }

    @BeforeEach
    void setUp() {
        this.em = EMF.createEntityManager();
        this.member = Member.builder()
                .name(UUID.randomUUID().toString().substring(0, 5))
                .build();
    }

    @AfterAll
    static void afterAll() {
        EMF.close();
    }

    @Test
    void name() {
        doInTransaction(this.em, () -> {
            this.em.persist(this.member);
            this.em.flush();
            this.em.clear();
        });
        assertNotNull(this.member.getId(), "ID 가 생성되어야 한다.");

        final Member fromEntityManager = this.em.find(Member.class, this.member.getId());
        assertNotNull(fromEntityManager);

        assertNotSame(this.member, fromEntityManager);

        final Member fromCache = this.em.find(Member.class, this.member.getId());

        assertSame(fromCache, fromEntityManager);
    }

    public <T> T doInTransaction(EntityManager em, Supplier<T> supplier) {
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            final T t = supplier.get();
            transaction.commit();
            return t;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void doInTransaction(EntityManager em, Runnable runnable) {
        try {
            doInTransaction(em, () -> {
                runnable.run();
                return Void.TYPE;
            });
        } catch (Exception e) {
            throw e;
        }
    }

}