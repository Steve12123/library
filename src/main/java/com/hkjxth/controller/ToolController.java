package com.hkjxth.controller;

import com.hkjxth.bean.JsonResult;
import com.hkjxth.bean.UtilClass;
import com.hkjxth.translate.TransApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.StringTokenizer;

@RestController
@RequestMapping("/toolController")
public class ToolController {
    @RequestMapping("/translate")
    public JsonResult translate(@RequestParam("translateChoice")String choice,
                                @RequestParam("translateBody")String translateBody){
        String temp = null;
        if (translateBody==null||translateBody.equals("")){
            return null;
        }else{
            TransApi api = new TransApi("20160607000022919", "666LECAoOcuiIv7xZe70");
            if (choice.equals("enToZh")){
                String str=api.getTransResult(translateBody, "auto", "zh");
                StringTokenizer token = new StringTokenizer(str,"\"}]}");
                while(token.hasMoreTokens()){
                    temp = token.nextToken();
                }
                String translateResult=UtilClass.decodeUnicode(temp);
                return JsonResult.success().add("translateResult",translateResult);
            }else{
                String str=api.getTransResult(translateBody, "auto", "en");
                StringTokenizer token = new StringTokenizer(str,"\"}]}");
                while(token.hasMoreTokens()){
                    temp = token.nextToken();
                }
                return JsonResult.success().add("translateResult",temp);
            }
        }
    }
}
