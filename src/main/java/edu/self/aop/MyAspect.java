package edu.self.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {
    @Pointcut("execution(* edu.self.web.controller.IndexController.*(..))")
    public void performance() {
    }

    @Before("performance()")
    public void takeSeats() {
        System.out.println("take seats!!!");
    }

    @Before("performance()")
    public void turnOffCellPhones() {
        System.out.println("turn off!!!");
    }

    @AfterReturning("performance()")
    public void applaud() {
        System.out.println("applaud!!!");
    }

    @AfterThrowing("performance()")
    public void demandRefund() {
        System.out.println("exception !!!");
    }

    @Around("performance()")
    public Object watchPerformance(ProceedingJoinPoint joinpoint) {
        Object result;
        try {
            System.out.println("Theaudienceistakingtheirseats.");
            System.out.println("Theaudienceisturningofftheircellphones");
            long start = System.currentTimeMillis();
            result = joinpoint.proceed();
            long end = System.currentTimeMillis();
            System.out.println("CLAPCLAPCLAPCLAPCLAP");
            System.out.println("Theperformancetook" + (end - start)
                    + "milliseconds.");
            return result;
        } catch (Throwable t) {
            System.out.println("Boo!Wewantourmoneyback!");
        }
        return null;
    }
}
