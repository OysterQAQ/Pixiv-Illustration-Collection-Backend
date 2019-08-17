package dev.cheerfun.pixivic.common.model;

import lombok.Data;

import java.util.List;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2019/08/17 17:41
 * @description Rank
 */
@Data
public class Rank {
    private List<Illustration> illustrations;
    private String mode;
    private String date;
}
