package org.ramer.diary.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *  通用JSON响应实体
 * Created by RAMER on 5/27/2017.
 */
@Data
@AllArgsConstructor
public class CommonResponse{
    @ApiModelProperty(notes = "返回结果", required = true)
    private boolean result;
    @ApiModelProperty(notes = "返回消息", required = true)
    private String message;
}
