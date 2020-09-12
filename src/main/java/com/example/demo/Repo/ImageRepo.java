package com.example.demo.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.ImageModel;

@Repository
public interface ImageRepo extends JpaRepository<ImageModel, Integer> {
	
    public List<ImageModel> findAllByOrderByIdDesc();
}
