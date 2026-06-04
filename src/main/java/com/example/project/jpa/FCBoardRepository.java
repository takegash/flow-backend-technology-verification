package com.example.project.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.jpa.entity.FCBoard;

@Repository
public interface FCBoardRepository extends JpaRepository<FCBoard, Integer> {
	FCBoard findTopByOrderByVerIdDesc();
}
