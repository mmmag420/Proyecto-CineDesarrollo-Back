package com.example.demo.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.example.demo.model.Chair;
import com.example.demo.model.Hall;

import jakarta.persistence.LockModeType;

public interface RepositorySilla extends JpaRepository<Chair, Integer>{

}
