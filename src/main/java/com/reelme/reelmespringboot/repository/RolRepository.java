package com.reelme.reelmespringboot.repository;

import com.reelme.reelmespringboot.model.Rol;
import org.springframework.data.repository.CrudRepository;

public interface RolRepository extends CrudRepository<Rol, Long> {
    Rol findByRol(String rol);

}
