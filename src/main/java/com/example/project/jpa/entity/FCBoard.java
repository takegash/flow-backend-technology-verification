package com.example.project.jpa.entity;

import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties.Json;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import tools.jackson.databind.JsonNode;

@Data
@Entity
@Table(name="fc_board")
public class FCBoard {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ver_id")
	private Integer verId;
	
	@Column(name="node_num")
	private Integer nodeNum;
	
	@Column(name="nodes", columnDefinition="json")
	@JdbcTypeCode(SqlTypes.JSON)
	private Map<String, Object> nodes;
	
	@Column(name="branch_num")
	private Integer branchNum;
	
	@Column(name="branches", columnDefinition="json")
	@JdbcTypeCode(SqlTypes.JSON)
	private Map<String, Object> branches;
}
