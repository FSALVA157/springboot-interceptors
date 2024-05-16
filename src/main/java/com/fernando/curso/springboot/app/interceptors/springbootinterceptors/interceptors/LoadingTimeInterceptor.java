package com.fernando.curso.springboot.app.interceptors.springbootinterceptors.interceptors;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("timeInterceptor")
public class LoadingTimeInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoadingTimeInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
                HandlerMethod controlador = (HandlerMethod) handler;        
                logger.info("Loading Time Interceptor: preHandle() entrando..." + controlador.getMethod().getName()) ;
                long start = System.currentTimeMillis();
                request.setAttribute("start", start);
                Random random = new Random();
                int delay = random.nextInt(500);
                Thread.sleep(delay);

                /*
                 * Porción de codigo que maneja la respuesta en caso de que el interceptor bloquee el paso
                 */
                Map<String, String> res = new HashMap<>();
                res.put("message", "No tienes acceso a este recurso!");
                res.put("fecha", new Date().toString());

                //manejemos res para convertirlo a JSON
                ObjectMapper mapper = new ObjectMapper();
                String jsonString = mapper.writeValueAsString(res);

                //configuremos la respuesta
                response.setContentType("applicatcion/json");
                response.setStatus(401);
                response.getWriter().write(jsonString);


                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                return false;
        //return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        HandlerMethod controlador = (HandlerMethod) handler;        
        logger.info("Loading Time Interceptor: postHandle() saliendo..." + controlador.getMethod().getName()) ;
        long end = System.currentTimeMillis();
        long start = (long) request.getAttribute("start");
        long duration = end - start;
        logger.info("Loading Time Interceptor: Duración: " + duration + " milisegundos");
        
    }

    

}
