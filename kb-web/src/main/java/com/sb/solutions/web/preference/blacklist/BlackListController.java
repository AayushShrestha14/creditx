package com.sb.solutions.web.preference.blacklist;

import java.util.List;

import com.sb.solutions.core.dto.SearchDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sb.solutions.api.preference.blacklist.entity.BlackList;
import com.sb.solutions.api.preference.blacklist.service.BlackListService;
import com.sb.solutions.api.preference.blacklist.util.BlackListExcelReader;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.PaginationUtils;
import com.sb.solutions.core.validation.constraint.FileFormatValid;


@RestController
@Validated
@RequestMapping("/v1/blacklist")
public class BlackListController {
    private final BlackListService blackListService;

    public BlackListController(
            @Autowired BlackListService blackListService) {
        this.blackListService = blackListService;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page.")})
    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllBlackList(@RequestParam("page") int page,
                                             @RequestParam("size") int size) {
        return new RestResponseDto().successModel(blackListService
                .findAllBlackList(PaginationUtils.pageable(page, size)));
    }
    @PostMapping(value = "/list")
    public ResponseEntity<?> getAll(@RequestBody SearchDto searchDto,
                                    @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto().successModel(blackListService
                .findAllPageable(searchDto, PaginationUtils.pageable(page, size)));
    }

    @PostMapping(value = "/uploadBlackList")
    public ResponseEntity<?> saveBlackListExcel(
            @RequestParam("excelFile") @FileFormatValid MultipartFile multipartFile) {

        List<BlackList> blackList = BlackListExcelReader.parseBlackListFile(multipartFile);
        if (blackList == null) {
            return new RestResponseDto().failureModel("Failed-Either file is empty or invalid file format");
        } else {
            blackListService.saveList(blackList);
            return new RestResponseDto().successModel("Added");
        }
    }

    @GetMapping(value = "/remove")
    public ResponseEntity<?> removeById(@RequestParam("id") Long id) {
        blackListService.removeById(id);

        return new RestResponseDto().successModel("Removed");
    }

    @GetMapping(value = "/checkBlacklistByRef")
    public ResponseEntity<?> checkBlackListedByRef(@RequestParam("ref") String ref) {
        return new RestResponseDto().successModel(
                blackListService.checkBlackListByRef(ref));
    }

}
