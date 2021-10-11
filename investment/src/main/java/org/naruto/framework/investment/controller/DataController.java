package org.naruto.framework.investment.controller;


import lombok.extern.java.Log;
import org.naruto.framework.core.web.ResultEntity;
import org.naruto.framework.investment.repository.Account;
import org.naruto.framework.investment.service.DataService;
import org.naruto.framework.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Log
@RestController
public class DataController {
    @Autowired
    private DataService dataService;

    @ResponseBody
    @RequestMapping(value = "/v1/extractFundNewShareAllocation", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResponseEntity<ResultEntity> extractFundNewShareAllocation(
            @RequestParam(required = false) String stockCode,
            HttpServletRequest request, HttpServletResponse response) {

        dataService.extractFundNewShareAllocation(stockCode);
        return ResponseEntity.ok(ResultEntity.ok(""));
    }
}
