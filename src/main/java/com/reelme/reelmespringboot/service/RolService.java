package com.reelme.reelmespringboot.service;

import com.reelme.reelmespringboot.model.Rol;
import com.reelme.reelmespringboot.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolService {
    @Autowired
    private RolRepository rolRepository;
    public void save(Rol rol) {
        rolRepository.save(rol);
    }

    public int count(){
        return (int) rolRepository.count();
    }
}
