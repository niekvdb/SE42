/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import bank.dao.AccountDAOJPAImpl;
import bank.domain.Account;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Niek
 */
public class Opdracht1 {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("bankPU");
    private EntityManager em = emf.createEntityManager();

    public Opdracht1() {
    }

    @Before
    public void setUp() throws SQLException {
        System.out.print("before");
        DatabaseCleaner dc = new DatabaseCleaner(this.em);
        dc.clean();
        // databasecleaner closes connection
        this.em = emf.createEntityManager();
    }

    /*      
     1.	Wat is de waarde van asserties en printstatements? Corrigeer verkeerde asserties zodat de test ‘groen’ wordt.
     -De eerste assert returned null, omdat er nog geen commit is uitgevoerd en het ID dus nog steeds NULL is.
     -In de tweede assert wordt er gecontroleerd of het account ID groter is dan 0.
     -Aangezien de commit al is geweest, is het ID van het account object dus bijgewerkt.
    
     2.	Welke SQL statements worden gegenereerd?
     -INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (?, ?, ?) bind => [111, 0, 0]
     -SELECT LAST_INSERT_ID()
    
     3.	Wat is het eindresultaat in de database?
     -  ID: 1 ACCOUNTNR: 111 BALANCE: 0 THRESHOLD: 0
     */
    @Test
    public void create() {
        Account account = new Account(111L);
        em.getTransaction().begin();
        em.persist(account);
        //Account is nog niet ge-commit, dus deze heeft nog geen ID toegewezen gekregen door de database.
        //Hierdoor zal 'account.getId()' 'null' returnen.
        assertNull(account.getId());
        em.getTransaction().commit();
        // waarde = 1
        System.out.println("AccountId: " + account.getId());
        //Nu is account ge-commit, waardoor deze een Id heeft gekregen.
        //'account.getId()' zal dus een waarde groter dan 0 hebben, waardoor deze assert 'true' is.
        assertTrue(account.getId() > 0L);
    }

    /*      
     1.	Wat is de waarde van asserties en printstatements? Corrigeer verkeerde asserties zodat de test ‘groen’ wordt.
     -De eerste assert returned null, omdat er geen commit is uitgevoerd dus ook nog geen account in de database staat
     -De tweede assert is true, omdat er na de rollback dus weer geen accounts in de database staan en de count dus 0 terug geeft
    
     2.	Welke SQL statements worden gegenereerd?
     -SELECT ID, ACCOUNTNR, BALANCE, THRESHOLD FROM ACCOUNT WHERE (ACCOUNTNR = 111)  (ROLLBACK)
     -select count(a) from Account as a - staat zo in Account.java
    
     3.	Wat is het eindresultaat in de database?
     -  geen rijen vanwege de rollback.
     */
    @Test
    public void Rollback() {
        Account account = new Account(111L);
        em.getTransaction().begin();
        em.persist(account);
        assertNull(account.getId());
        em.getTransaction().rollback();
        // TODO code om te testen dat table account geen records bevat. Hint: bestudeer/gebruik AccountDAOJPAImpl
        //De klasse AccountDAOImpl gebruikt een named query om het aantal Accounts op te halen.
        //Deze wordt zo gebruikt om het aantal accounts te checken, wat dus 0 moet zijn.
        AccountDAOJPAImpl accDAO = new AccountDAOJPAImpl(em);
        assertTrue(accDAO.count() == 0);
    }

    /*      
     1.	Wat is de waarde van asserties en printstatements? Corrigeer verkeerde asserties zodat de test ‘groen’ wordt.
     -De eerste assert returned true, omdat de gesette ID nog gelijk is aan de account ID omdat er nog niks gecommit is naar de database 
     -De tweede assert is true, nu wel gesynced met de database (na de flush)id is gemarkeerd als auto generated dus zal nu niet hetzelfde zijn als de expected long
    
     2.	Welke SQL statements worden gegenereerd?
     -INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (111, 0, 0) na de flush
     -SELECT LAST_INSERT_ID() na de flush

    
     3.	Wat is het eindresultaat in de database?
     -EINDRESULTAAT:
     -In de database staat nu een rij in de tabel account met de waardes:
     -ID = 1, ACCOUNTNR = 111, BALANCE = 0, THRESHOLD = 0
     */
    @Test
    public void Flush() {
        Long expected = -100L;
        Account account = new Account(111L);
        account.setId(expected);
        em.getTransaction().begin();
        em.persist(account);
        //TODO: verklaar en pas eventueel aan
        //ID is geset naar -100L en nog niet gepushed naar de database dus de auto generate is nog niet van pas gekomen. Expected=account.getId()
        assertEquals(expected, account.getId());
        em.flush();
        //TODO: verklaar en pas eventueel aan
        //Data nu wel gesynced met de database (na de flush)  id is gemarkeerd als auto generated dus zal nu niet hetzelfde zijn als de expected long
        //And it is not the same as expected anymore
        assertNotEquals(expected, account.getId());
        em.getTransaction().commit();
    }

    /*      
     1.	Wat is de waarde van asserties en printstatements? Corrigeer verkeerde asserties zodat de test ‘groen’ wordt.
     -De eerste assert returned true, de verwachte balans geset is naar 400L en ook gecommit dus staat ook in de database zo.
     -De tweede assert is true,Het account, en dus de balans, wordt uit de database gehaald en zou dezelfde waarde moeten hebben als die van expectedBalance
    
     2.	Welke SQL statements worden gegenereerd?
     -SELECT ID, ACCOUNTNR, BALANCE, THRESHOLD FROM ACCOUNT WHERE (ID = 1) bij het zoeken naar account

    
     3.	Wat is het eindresultaat in de database?
     -In de database staat nu een rij in de tabel account met de waardes:
     -ID = 1, ACCOUNTNR = 114, BALANCE = 400, THRESHOLD = 0
     */
    @Test
    public void changesAfterPersist() {
        Long expectedBalance = 400L;
        Account account = new Account(114L);
        em.getTransaction().begin();
        em.persist(account);
        account.setBalance(expectedBalance);
        em.getTransaction().commit();
        assertEquals(expectedBalance, account.getBalance());
        //TODO: verklaar de waarde van account.getBalance
        Long acId = account.getId();
        account = null;
        EntityManager em2 = emf.createEntityManager();
        em2.getTransaction().begin();
        Account found = em2.find(Account.class, acId);
        //TODO: verklaar de waarde van found.getBalance
        // waarde = 400
        assertEquals(expectedBalance, found.getBalance());
    }

    /*      
     1.	Wat is de waarde van asserties en printstatements? Corrigeer verkeerde asserties zodat de test ‘groen’ wordt.
     -De eerste assert returned true, de verwachte balans geset is naar 400L en ook gecommit dus staat ook in de database zo.
     -De tweede assert is true.Het account, en dus de balans, wordt uit de database gehaald en zou dezelfde waarde moeten hebben als die van expectedBalance
     -De derde assert is ook true, omdat account1 zijn balans gelijk is aan account 2 omdat het dezelfde rij is.
    
     2.	Welke SQL statements worden gegenereerd?
     -INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (114, 0, 0)
     -UPDATE ACCOUNT SET BALANCE = 400 WHERE (ID = 2)
     -SELECT ID, ACCOUNTNR, BALANCE, THRESHOLD FROM ACCOUNT WHERE (ID = 2)  bij de em2.find functie
     -UPDATE ACCOUNT SET BALANCE = 300 WHERE (ID = 2)
    
    
     3.	Wat is het eindresultaat in de database?
     -In de database staat nu een rij in de tabel account met de waardes:
     ID = 2, ACCOUNTNR = 114, BALANCE = 300, THRESHOLD = 0
     */
    @Test
    public void refreshTest() {
        Long expectedBalance = 400L;
        Account account1 = new Account(114L);
        em.getTransaction().begin();
        em.persist(account1);
        account1.setBalance(expectedBalance);
        //bij deze commit gebeuren sql statements 1 en 2. Zie hierboven
        em.getTransaction().commit();
        assertEquals(expectedBalance, account1.getBalance());
        EntityManager em2 = emf.createEntityManager();
        em2.getTransaction().begin();
        //bij deze commit gebeurd sql statement 3. Zie hierboven
        Account account2 = em2.find(Account.class, account1.getId());
        // waarde = 400
        assertEquals(expectedBalance, account2.getBalance());
        em2.persist(account2);
        account2.setBalance(account2.getBalance() - 100);
        //bij deze commit gebeurd sql statement 4. Zie hierboven
        em2.getTransaction().commit();
        //bij deze refresh gebeurt sql statement 2. Zie hierboven
        em.refresh(account1);
        // waarde = true
        assertEquals(account1.getBalance(), account2.getBalance());

    }
}
