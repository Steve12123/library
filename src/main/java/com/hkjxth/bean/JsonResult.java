package com.hkjxth.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonResult {
    //提示信息
    private String message;
    //用户返回给浏览器的数据
    private Map<String, Object> extend=new HashMap<String, Object>();

    public static JsonResult success(){
        JsonResult jsonResult=new JsonResult();
        jsonResult.setMessage("处理成功");
        return jsonResult;
    }

    public static JsonResult fail(){
        JsonResult jsonResult=new JsonResult();
        jsonResult.setMessage("处理失败");
        return jsonResult;
    }

    public JsonResult add(String key, Object value){
        this.getExtend().put(key, value);
        return this;
    }
}

