package CMS.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import CMS.Master.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
    // Additional query methods can be defined here if needed
}
