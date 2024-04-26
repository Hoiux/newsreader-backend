package org.hoiux.newsreader.repository;

import org.hoiux.newsreader.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {


}