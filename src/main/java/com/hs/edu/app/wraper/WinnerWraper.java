package com.hs.edu.app.wraper;

import com.hs.edu.app.entity.Gamer;
import lombok.Data;

import java.util.List;

/**
 * @ClassName GamerMsgWraper
 * @Description TODO
 * @Author taxuefan
 * @Date 2020/10/14 17:10
 * @Version 1.0
 **/
@Data
public class WinnerWraper {
    private int gameStatus;
    private Winner winner;
}
