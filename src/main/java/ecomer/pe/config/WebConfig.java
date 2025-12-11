package ecomer.pe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new NoCacheInterceptor())
                .addPathPatterns("/**") // aplica a todas las rutas
                .excludePathPatterns("/", "/compras", "/login", "/registro", "/css/**", "/js/**", "/img/**", "/uploads/**");
    }
}

