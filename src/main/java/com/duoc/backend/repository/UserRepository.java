package com.duoc.backend.repository;

import org.springframework.data.repository.CrudRepository;

import com.duoc.backend.model.User;

// Este código será CREADO AUTOMATICAMENTE por Spring en un Bean llamado userRepository
// CRUD significa Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

}
