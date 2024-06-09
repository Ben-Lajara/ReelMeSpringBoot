package com.reelme.reelmespringboot.service;

import com.reelme.reelmespringboot.repository.TokenPwordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenPwordService {
    @Autowired
    private TokenPwordRepository tokenPwordRepository;

    @Scheduled(fixedRate = 300000)
    @Transactional
    public void borrarTokensExpirados() {
        tokenPwordRepository.deleteByFechaExpiracionBefore(new Date());
    }
}
