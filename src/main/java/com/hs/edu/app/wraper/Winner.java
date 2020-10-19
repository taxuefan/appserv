package com.hs.edu.app.wraper;

import lombok.Data;

/**
 * @ClassName WinnerWraper
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/14 16:08
 * @Version 1.0
 **/
@Data
public class Winner {
    private String role;//赢家对应的角色
    private String pingMin;//卧底词
    private String wodi; //平民词
    private String seqNo;//游戏号
}
