package com.yx.controller;

import com.yx.bean.Person;
import com.yx.service.PersonService;
import com.yx.utils.GenResult;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping(value = "person")
public class PersonController {

    private static Logger log = Logger.getLogger(PersonController.class);

    @Resource
    private PersonService personService;

    //test:/person/addPerson.do?id=1&name=jack&address=bj
    @RequestMapping(value = "addPerson")
    @ResponseBody
    public Map<String, Object> addPerson(Person person) {
        boolean result = personService.addPerson(person);
        if (result) {
            return GenResult.SUCCESS.genResult();
        } else {
            return GenResult.FAILED.genResult();
        }
    }

    //test: /person/getPersonById.do?id=1
    @RequestMapping(value = "getPersonById")
    @ResponseBody
    public Map<String, Object> getPersonById(int id) {
        Person person = personService.getPersonById(id);
        return GenResult.SUCCESS.genResult(person);
    }

    //test: /person/delete.do?key=1
    @RequestMapping(value = "delete")
    @ResponseBody
    public Map<String, Object> delete(String key) {
        personService.delete(key);
        return GenResult.SUCCESS.genResult();
    }

    //test: /person/update.do?id=1&name=newName&address=bj
    @RequestMapping(value = "update")
    @ResponseBody
    public Map<String, Object> update(Person person) {
        boolean result = personService.update(person);
        if (result) {
            return GenResult.SUCCESS.genResult();
        }
        return GenResult.FAILED.genResult();
    }

    //test: /person/testMsgPack.do
    @RequestMapping(value = "testMsgPack")
    @ResponseBody
    public Map<String, Object> testMsgPack( ) {
        boolean result = personService.testMsgPack();
        return GenResult.SUCCESS.genResult(result);
    }
}
