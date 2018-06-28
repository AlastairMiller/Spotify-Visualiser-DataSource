package al.miller.cv.ds.mapper;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class RefinedtoJsonMapper {

    public static Object invokeSimpleGetter(Method getter, Object inputObject) {
        try {
            Object nativeValue = getter.invoke(inputObject);
            if (getter.getReturnType().getName().equals("java.net.URL") | getter.getReturnType().getName().equals("java.net.URI")) {
                return nativeValue.toString();
            }
            return nativeValue;
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage());
        } catch (NullPointerException e){
            log.error("PreviewURL is null");
        }
        return null;
    }
}
