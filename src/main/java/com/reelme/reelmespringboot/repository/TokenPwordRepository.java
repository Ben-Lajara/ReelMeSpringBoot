package com.reelme.reelmespringboot.repository;

import com.reelme.reelmespringboot.model.TokenPword;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface TokenPwordRepository extends CrudRepository<TokenPword, String> {
    TokenPword findByToken(String token);
    void deleteByFechaExpiracionBefore(Date fechaExpiracion);
}
