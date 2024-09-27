package com.mg.core.dto.sp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "공통 DTO")
public class CommonDTO {

    @Schema(description = "생성자")
    private String insId;
    @Schema(description = "생성일")
    private String insDt;
    @Schema(description = "수정자")
    private String uptId;
    @Schema(description = "수정일")
    private String uptDt;

}
