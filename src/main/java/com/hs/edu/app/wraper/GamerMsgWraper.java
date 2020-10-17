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
public class GamerMsgWraper {
    private int gameStatus;
    private List<Gamer> users=new java.util.ArrayList<Gamer>();
}
