package org.ramer.diary.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *  通用JSON响应实体
 * Created by RAMER on 5/27/2017.
 */
@Data
@AllArgsConstructor
public class CommonsResponse{
    private boolean success;
    private String message;
}
