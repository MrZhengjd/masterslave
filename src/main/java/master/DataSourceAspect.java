package master;

import common.HandleDataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class DataSourceAspect {

    public void before(JoinPoint point){
        Object target = point.getTarget();
        String method = point.getSignature().getName();
        Class<?>[] clazzs = target.getClass().getInterfaces();
        Class<?>[] parameterTypes = ((MethodSignature)point.getSignature()).getMethod().getParameterTypes();
        try {
            Method m = clazzs[0].getMethod(method,parameterTypes);
            if (m != null && m.isAnnotationPresent(DataSourceAn.class)){
                DataSourceAn dataSource = m.getAnnotation(DataSourceAn.class);
                System.out.println("here is the sql type "+dataSource.name());
                HandleDataSource.putDataSource(dataSource.name());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
