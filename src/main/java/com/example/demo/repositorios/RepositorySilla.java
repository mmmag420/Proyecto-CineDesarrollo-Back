package com.example.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Chair;

public interface RepositorySilla extends JpaRepository<Chair, Integer>{

}
