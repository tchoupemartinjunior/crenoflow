package com.tchoupe.crenoflow.service;

import com.tchoupe.crenoflow.domain.Person;
import com.tchoupe.crenoflow.repository.PersonRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.tchoupe.crenoflow.domain.Person}.
 */
@Service
@Transactional
public class PersonService {

    private final Logger log = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Save a person.
     *
     * @param person the entity to save.
     * @return the persisted entity.
     */
    public Person save(Person person) {
        log.debug("Request to save Person : {}", person);
        return personRepository.save(person);
    }

    /**
     * Update a person.
     *
     * @param person the entity to save.
     * @return the persisted entity.
     */
    public Person update(Person person) {
        log.debug("Request to update Person : {}", person);
        return personRepository.save(person);
    }

    /**
     * Partially update a person.
     *
     * @param person the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Person> partialUpdate(Person person) {
        log.debug("Request to partially update Person : {}", person);

        return personRepository
            .findById(person.getId())
            .map(existingPerson -> {
                if (person.getFirstName() != null) {
                    existingPerson.setFirstName(person.getFirstName());
                }
                if (person.getLastName() != null) {
                    existingPerson.setLastName(person.getLastName());
                }
                if (person.getEmail() != null) {
                    existingPerson.setEmail(person.getEmail());
                }
                if (person.getPhoneNumber() != null) {
                    existingPerson.setPhoneNumber(person.getPhoneNumber());
                }
                if (person.getAddress() != null) {
                    existingPerson.setAddress(person.getAddress());
                }
                if (person.getCity() != null) {
                    existingPerson.setCity(person.getCity());
                }
                if (person.getPostalCode() != null) {
                    existingPerson.setPostalCode(person.getPostalCode());
                }
                if (person.getBirthday() != null) {
                    existingPerson.setBirthday(person.getBirthday());
                }

                return existingPerson;
            })
            .map(personRepository::save);
    }

    /**
     * Get all the people.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Person> findAll(Pageable pageable) {
        log.debug("Request to get all People");
        return personRepository.findAll(pageable);
    }

    /**
     * Get all the people with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Person> findAllWithEagerRelationships(Pageable pageable) {
        return personRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     *  Get all the people where Teacher is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Person> findAllWhereTeacherIsNull() {
        log.debug("Request to get all people where Teacher is null");
        return StreamSupport.stream(personRepository.findAll().spliterator(), false).filter(person -> person.getTeacher() == null).toList();
    }

    /**
     *  Get all the people where PhotoPerson is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Person> findAllWherePhotoPersonIsNull() {
        log.debug("Request to get all people where PhotoPerson is null");
        return StreamSupport
            .stream(personRepository.findAll().spliterator(), false)
            .filter(person -> person.getPhotoPerson() == null)
            .toList();
    }

    /**
     * Get one person by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Person> findOne(Long id) {
        log.debug("Request to get Person : {}", id);
        return personRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the person by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Person : {}", id);
        personRepository.deleteById(id);
    }
}
