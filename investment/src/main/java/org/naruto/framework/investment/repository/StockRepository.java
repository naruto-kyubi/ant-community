package org.naruto.framework.investment.repository;

import org.naruto.framework.core.repository.CustomRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface StockRepository extends CustomRepository<Stock,String>{
//    Stock queryStockByCode(String code);
}