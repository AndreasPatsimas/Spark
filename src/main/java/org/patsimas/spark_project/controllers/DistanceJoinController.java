package org.patsimas.spark_project.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.patsimas.spark_project.dto.DistanceJoinDto;
import org.patsimas.spark_project.services.DistanceJoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RequestMapping(value = "/distance-joins")
@RestController
@Slf4j
@Api(description = "DistanceJoin")
public class DistanceJoinController {

    @Autowired
    private DistanceJoinService distanceJoinService;

    @ApiOperation(value = "Find Distance Joins for specific value.", response = DistanceJoinDto.class)
    @PostMapping(value = "/{value}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<DistanceJoinDto> importCsv(@RequestParam("fileOne") MultipartFile fileOne,
                                          @RequestParam("fileTwo") MultipartFile fileTwo,
                                          @PathVariable("value") Long value) {

        log.info("Fetch distance joins for value: {}", value);
        return distanceJoinService.fetchDistanceJoins(fileOne, fileTwo, value);
    }
}
