package com.mg.api.sample;

import com.mg.core.common.annotation.PrimaryId;
import com.mg.core.common.annotation.MGRestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@MGRestController
@Slf4j
@Tag(name = "TEST", description = "For Test")
@RequestMapping("/api")
public class SampleController {

    @GetMapping("/sample")
    public Map<String, Object> sample() {
        return Map.of("sample1", "sample1", "sample2", "sample2");
    }

    @Operation(summary = "@PrimaryId 어노테이션 테스트", description = "로그인한 정보의 uid를 반환한다.")
    @GetMapping("/test/primaryId")
    public Map<String, Object> testPrimaryId(@PrimaryId @Parameter(hidden = true) String uid) {
        log.info("""
                uid - {}
                """, uid);
        return Map.of("uid", uid);
    }

}
