package com.hkjxth.bean;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilClass {
    /**
     * 工具类
     * getDate:获取当前时间 返回String
     * savePhoto 返回图片路径 args1:接收file args2:自定义名字
     * ajaxUTF8 处理ajax中文乱码
     * deletePhoto 需传入/bookimg/......jpg格式的String
     * @return
     */
    public static final String getDateToDatabase(){
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
        String time=simpleDateFormat.format(date);
        return time;
    }

    public static final String getDateToLocal(){
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
        String time=simpleDateFormat.format(date);
        return time;
    }


    public static final String getDate(){
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String time=simpleDateFormat.format(date);
        return time;
    }

    public static final String savePhoto(MultipartFile file, String divFileName){
        if(file.isEmpty()){
            System.out.println("空");
            return null;
        }else{
            String fileName=file.getOriginalFilename();
            String suffix=fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
            if(divFileName==null||divFileName==""){
                fileName= "lib"+getDateToLocal()+suffix;
            }else{
                fileName=divFileName+suffix;
            }
            File newFile=new File("D:\\librarySYSPhoto\\bookPhoto\\"+fileName);
            if(!newFile.getParentFile().exists()){
                newFile.getParentFile().mkdirs();
            }
            try {
                file.transferTo(newFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String filename="/bookimg/"+fileName;
            System.out.println("图片虚拟路径为"+filename);
            return filename;
        }
    }

    public static final void deletePhoto(String photoName) {
        if(!photoName.equals("empty")){
            File file = new File("D:\\librarySYSPhoto\\bookPhoto\\" + photoName.substring(9));
            file.delete();
        }

    }

    public static final String ajaxUTF8(String name) throws UnsupportedEncodingException {
        return URLDecoder.decode(name,"utf-8");
    }

}

