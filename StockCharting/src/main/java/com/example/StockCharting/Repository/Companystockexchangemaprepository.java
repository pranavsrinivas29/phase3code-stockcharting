package com.example.StockCharting.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.StockCharting.Entity.Companystockexchangemap;

@Repository
public interface Companystockexchangemaprepository extends JpaRepository<Companystockexchangemap, Long> {

	Companystockexchangemap findByCodeAndName(String companycode, String stockexchangename);

	Companystockexchangemap findByCompanyNameAndExchangeName(String companyname, String stockexchangename);
}