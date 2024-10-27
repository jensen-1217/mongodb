package com.sl.mongo.service.impl;

import com.sl.mongo.entity.Person;
import com.sl.mongo.service.PersonService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jensen
 * @date 2024-10-28 0:14
 */
@Service
public class PersonServiceImpl implements PersonService {
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void savePerson(Person person) {
        this.mongoTemplate.save(person);
    }

    @Override
    public void update(Person person) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(person.getId()));
        Update update = new Update();
        update.set("age", person.getAge())
                .set("name", person.getName())
                .set("location", person.getLocation())
                .set("address", person.getAddress());
        this.mongoTemplate.updateFirst(query,update,Person.class);
    }

    @Override
    public List<Person> queryPersonListByName(String name) {
        List<Person> people = mongoTemplate.find(Query.query(Criteria.where("name").regex(name)), Person.class);
        return people;
    }

    @Override
    public List<Person> queryPersonPageList(int page, int pageSize) {
        List<Person> people = mongoTemplate.find(new Query().with(Sort.by(Sort.Direction.DESC,"age")).with(PageRequest.of(page-1,pageSize)), Person.class);
        return people;
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)),Person.class);
    }
}
