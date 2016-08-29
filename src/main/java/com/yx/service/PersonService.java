package com.yx.service;

import com.yx.bean.Person;
import com.yx.dao.PersonDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PersonService {

    @Resource
    private PersonDao personDao;

    public boolean addPerson(Person person) {
        return personDao.addPerson(person);
    }

    public void delete(String key) {
        personDao.delete(key);
    }

    public boolean update(Person person) {
        return personDao.update(person);
    }

    public Person getPersonById(int id) {
        return personDao.getPersonById(id);
    }

    public boolean testMsgPack( ) {
        return personDao.testMsgPack();
    }
}
