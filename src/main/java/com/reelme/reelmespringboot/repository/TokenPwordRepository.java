package com.reelme.reelmespringboot.repository;

import com.reelme.reelmespringboot.model.TokenPword;
import org.springframework.data.repository.CrudRepository;

public interface TokenPwordRepository extends CrudRepository<TokenPword, String> {
    TokenPword findByToken(String token);
}
