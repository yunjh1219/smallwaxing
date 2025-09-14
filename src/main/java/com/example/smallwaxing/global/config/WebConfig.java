package com.example.smallwaxing.global.config;

import com.example.smallwaxing.global.util.LoginUserArgumentResolver;
import com.example.smallwaxing.global.util.RefreshTokenArgumentResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.physical-dir}")           // ✅ 키 통일
    private String physicalDir;

    @Value("${app.upload.url-prefix:/uploads/}")   // ✅ 키 통일
    private String urlPrefix;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080") // 클라이언트 도메인
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new RefreshTokenArgumentResolver());
        resolvers.add(new LoginUserArgumentResolver());
    }

    /**
     * 🔹 업로드한 파일을 정적 리소스로 제공
     * 예) C:/smallwaxing/uploads/notice/a.png
     *  → http://localhost:8080/uploads/notice/a.png
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String pattern  = urlPrefix.endsWith("/") ? urlPrefix + "**" : urlPrefix + "/**";
        String location = "file:" + Paths.get(physicalDir).toAbsolutePath() + "/";

        registry.addResourceHandler(pattern)
                .addResourceLocations(location);
    }
}
