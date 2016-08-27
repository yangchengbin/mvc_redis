package com.yx.controller;

import com.yx.bean.Person;
import com.yx.service.PersonService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "person")
public class PersonController {

    @Resource
    private PersonService personService;

    //test:/person/addPerson.do?id=1&name=jack&address=bj
    @RequestMapping(value = "addPerson")
    @ResponseBody
    public Map<String, Object> addPerson(Person person) {
        Map<String, Object> obj = new HashMap<String, Object>();
        boolean result = personService.addPerson(person);
        obj.put("result", result);
        return obj;
    }

    //test: /person/getPersonById.do?id=1
    @RequestMapping(value = "getPersonById")
    @ResponseBody
    public Map<String, Object> getPersonById(int id) {
        Map<String, Object> obj = new HashMap<String, Object>();
        Person person = personService.getPersonById(id);
        obj.put("data", person);
        obj.put("result", "success");
        return obj;
    }

    //test: /person/delete.do?key=1
    @RequestMapping(value = "delete")
    @ResponseBody
    public Map<String, Object> delete(String key) {
        Map<String, Object> obj = new HashMap<String, Object>();
        personService.delete(key);
        obj.put("result", "success");
        return obj;
    }

    //test: /person/update.do?id=1&name=newName&address=bj
    @RequestMapping(value = "update")
    @ResponseBody
    public Map<String, Object> update(Person person) {
        Map<String, Object> obj = new HashMap<String, Object>();
        try {
            boolean result = personService.update(person);
            obj.put("result", result);
        } catch (Exception e) {
            obj.put("result", e.getMessage());
        }
        return obj;
    }
}
