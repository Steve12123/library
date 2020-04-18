package com.hkjxth.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        WebMvcConfigurer adapter=new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/index.html").setViewName("/");
                registry.addViewController("/mainuser.html").setViewName("user/personlist");
                registry.addViewController("/mainroot.html").setViewName("root/rooterinfo");
                registry.addViewController("/register.html").setViewName("register");

                registry.addViewController("/userinfopage.html").setViewName("user/userinfo/userinfopage");
                registry.addViewController("/userpassword.html").setViewName("user/userinfo/userpasswordpage");
                registry.addViewController("/userwaiting.html").setViewName("user/userinfo/userwaitingpage");
                registry.addViewController("/userinfomation.html").setViewName("user/userinfo/userinformationpage");

                registry.addViewController("/talking.html").setViewName("user/persontalking");
                registry.addViewController("/booklist.html").setViewName("user/personbooklist");

                registry.addViewController("/useradmin.html").setViewName("root/rootuserlistpage");
                registry.addViewController("/userreturnbook.html").setViewName("root/rootreturnbookpage");
                registry.addViewController("/userbookrecord.html").setViewName("root/rootbookrecordpage");
                registry.addViewController("/userleavetime.html").setViewName("root/rootleavetime");
                registry.addViewController("/advicemessage.html").setViewName("root/rootmessageset");

                registry.addViewController("/createTalking.html").setViewName("user/usertalking/createtalking");
                registry.addViewController("/talkingInfo.html").setViewName("user/usertalking/talkinginfo");
                registry.addViewController("/mineTalking.html").setViewName("user/usertalking/mytalking");

                registry.addViewController("/showUser.html").setViewName("showuser");
                registry.addViewController("/showUserByRooter.html").setViewName("root/showuserbyrooter");
            }

            /*配置虚拟路径*/
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/bookimg/**").addResourceLocations("file:D:/librarySYSPhoto/bookPhoto/");
            }
        };
        return adapter;
    }

    //注册loginInterceptor拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginControllerInterceptor()).addPathPatterns("/**")
        .excludePathPatterns("/","/login","/static/**","/register");
    }
}
