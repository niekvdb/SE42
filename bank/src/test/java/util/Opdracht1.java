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

    /*      
     1.	Wat is de waarde van asserties en printstatements? Corrigeer verkeerde asserties zodat de test ‘groen’ wordt.
     -De eerste assert returned true, de verwachte balans geset is naar 100L en ook gecommit dus staat ook in de database zo.
     -De tweede assert returned true, account staat in de database met nummer 1L
    
    
     2.	Welke SQL statements worden gegenereerd?
     -INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (1, 100, 0)
     -select a from Account as a where a.accountNr = :accountNr
     
    
     3.	Wat is het eindresultaat in de database?
     -
     */
    @Test
    public void mergeTest() {
        Account acc = new Account(1L);
        Account acc2 = new Account(2L);
        Account acc9 = new Account(9L);
        AccountDAOJPAImpl dao = new AccountDAOJPAImpl(em);

        // scenario 1
        Long balance1 = 100L;
        em.getTransaction().begin();
        em.persist(acc);
        acc.setBalance(balance1);
        //Hier wordt sql statement 1 uitgevoerd. Zie hierboven
        em.getTransaction().commit();

        //asserties part 1
        assertEquals(balance1, acc.getBalance());
        assertEquals(new Long(0), acc.getThreshold());
        assertEquals(new Long(1), acc.getAccountNr());
        //Hier wordt sql statement 2 uitgevoerd. Zie hierboven
        Account found1 = dao.findByAccountNr(1L);
        //asserties part 2
        assertEquals(found1, acc);
        assertEquals(balance1, acc.getBalance());
        assertEquals(new Long(0), acc.getThreshold());
        assertEquals(new Long(1), acc.getAccountNr());

        //TODO: voeg asserties toe om je verwachte waarde van de attributen te verifieren.
        //TODO: doe dit zowel voor de bovenstaande java objecten als voor opnieuw bij de entitymanager opgevraagde objecten met overeenkomstig Id.
        // scenario 2
        Long balance2a = 211L;
        acc = new Account(10L);
        em.getTransaction().begin();
        acc9 = em.merge(acc);
        acc.setBalance(balance2a);
        acc9.setBalance(balance2a + balance2a);
        em.getTransaction().commit();

        //asserties part 1
        assertEquals(new Long(10), acc.getAccountNr());
        assertEquals(new Long(0), acc.getThreshold());
        assertEquals(balance2a, acc.getBalance());
        assertEquals(new Long(balance2a + balance2a), acc9.getBalance());

        //asserties part 2
        Account found2 = dao.findByAccountNr(acc.getAccountNr());
        assertEquals(new Long(10), found2.getAccountNr());
        assertEquals(new Long(0), found2.getThreshold());
        assertEquals(new Long(balance2a + balance2a), found2.getBalance());
        assertEquals(new Long(balance2a + balance2a), acc9.getBalance());
        assertNotEquals(acc.getBalance(), acc9.getBalance());

        //TODO: voeg asserties toe om je verwachte waarde van de attributen te verifiëren.
        //TODO: doe dit zowel voor de bovenstaande java objecten als voor opnieuw bij de entitymanager opgevraagde objecten met overeenkomstig Id. 
        // HINT: gebruik acccountDAO.findByAccountNr
        // scenario 3
        Long balance3b = 322L;
        Long balance3c = 333L;
        acc = new Account(3L);
        em.getTransaction().begin();
        Account acc3 = em.merge(acc);
        // assertTrue(em.contains(acc)); // verklaar: acc bestaat niet meer in de database, omdat die gemerged is met acc3
        assertTrue(em.contains(acc3));  // verklaar: acc3 heeft alle waardes van acc gekregen en bestaat nog in de database.
        //  assertEquals(acc, acc3);  // verklaar: acc bestaat niet meer in de database, omdat die gemerged is met acc3
        acc3.setBalance(balance3b);
        acc.setBalance(balance3c);
        //INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (3, 322, 0)
        em.getTransaction().commit();

        //asserties part 1
        Account found3 = dao.findByAccountNr(3L);
        assertEquals(balance3b, found3.getBalance());

        // scenario 4
        Account account = new Account(114L);
        account.setBalance(450L);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(account);
        //INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (114, 450, 0)
        em.getTransaction().commit();

        Account account2 = new Account(114L);
        Account tweedeAccountObject = account2;
        tweedeAccountObject.setBalance(650l);
        assertEquals((Long) 650L, account2.getBalance());  //verklaar: de twee accounts zijn in java gemerged, door tweedeAccountObject = account2;
        account2.setId(account.getId());
        em.getTransaction().begin();
        account2 = em.merge(account2);
        assertSame(account, account2);  //account en account2 hebben hetzelfde ID
        assertTrue(em.contains(account2));  //verklaar account2 is managed door em2.
        assertFalse(em.contains(tweedeAccountObject));   //verklaar: tweedeAccountObject is nooit in de database gezet.
        tweedeAccountObject.setBalance(850l);
        assertEquals((Long) 650L, account.getBalance());  //verklaar: account is met account2 in java "gemerged", hierdoor is de balans van account hetzelfde als die van account2 geworden.
        assertEquals((Long) 650L, account2.getBalance());  //verklaar: het balans van account2 is nooit veranderd.
        //INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (114, 650, 0)
        em.getTransaction().commit();
        em.close();

    }

    @Test
    public void FindAndClearTest() {
        Account acc1 = new Account(77L);
        em.getTransaction().begin();
        em.persist(acc1);
        //INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (77, 0, 0)
        em.getTransaction().commit();
        //Database bevat nu een account.

        // scenario 1        
        Account accF1;
        Account accF2;
        accF1 = em.find(Account.class, acc1.getId());
        accF2 = em.find(Account.class, acc1.getId());
        assertSame(accF1, accF2);

        // scenario 2        
        accF1 = em.find(Account.class, acc1.getId());
        //clear detached als het ware alle gemanagede entities. dus acc1 word gedetached en daardoor geeft de 2de find niet meer hetzelfde terug als de eerste
        em.clear();
        accF2 = em.find(Account.class, acc1.getId());
//        assertSame(accF1, accF2);
        //TODO verklaar verschil tussen beide scenario's

        /**
         * De veranderingen die zijn gemaakt aan de database, zijn niet
         * geflushed. Doordat in het 2de scenario em.clear(); wordt aangeroepen,
         * worden alle veranderingen die aan de database zijn aangebracht niet
         * gepersisteerd.
         */
    }

    @Test
    public void RemoveTest() {
        Account acc1 = new Account(88L);
        em.getTransaction().begin();
        em.persist(acc1);
        //INSERT INTO ACCOUNT (ACCOUNTNR, BALANCE, THRESHOLD) VALUES (88, 0, 0)
        em.getTransaction().commit();
        Long id = acc1.getId();
        //Database bevat nu een account.

        em.remove(acc1);
        assertEquals(id, acc1.getId());
        Account accFound = em.find(Account.class, id);
        assertNull(accFound);
        //TODO: verklaar bovenstaande asserts

        /**
         * Het account is alleen uit de database verwijderd. Het kan dus wel in
         * java gevonden worden, maar niet in de database. Dus de eerste assert
         * kijkt binnen java en de tweede probeert een account te zoeken in de
         * database
         */
    }

    /**
     * Opdracht 9 bij GenerationType TABLE faalde de test hoe die nu geschreven
     * is omdat bij de andere 2 generation types de Primary key door de database
     * wordt gegenereerd. Bij Table wordt primary key gegenereerd tijdens de
     * persist.
     */
}
