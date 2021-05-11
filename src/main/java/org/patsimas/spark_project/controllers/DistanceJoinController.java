package org.patsimas.spark_project.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.patsimas.spark_project.dto.DistanceJoinDto;
import org.patsimas.spark_project.services.DistanceJoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping(value = "/distance-joins")
@RestController
@Api(description = "DistanceJoin")
public class DistanceJoinController {

    @Autowired
    private DistanceJoinService distanceJoinService;

    @ApiOperation(value = "Find Distance Joins for specific value.", response = DistanceJoinDto.class)
    @GetMapping(value = "/{value}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<DistanceJoinDto> importCsv(@PathVariable("value") Double value) {

        return distanceJoinService.fetchDistanceJoins(value);
    }
}
