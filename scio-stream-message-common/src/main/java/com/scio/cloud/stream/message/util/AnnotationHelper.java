package com.scio.cloud.stream.message.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
/**
 * AnnotaionHelper to alter annotation value at runtime
 *
 * @author Wang.ch
 * @date 2019-02-02 09:04:47
 */
public class AnnotationHelper {
  public static final String ANNOTATIONS = "annotations";
  public static final String ANNOTATION_DATA = "annotationData";
  private static final String DECLARED_ANNOTATIONS = "declaredAnnotations";

  public static boolean isJDK7OrLower() {
    boolean jdk7OrLower = true;
    try {
      Class.class.getDeclaredField(ANNOTATIONS);
    } catch (NoSuchFieldException e) {
      // Willfully ignore all exceptions
      jdk7OrLower = false;
    }
    return jdk7OrLower;
  }

  @SuppressWarnings("unchecked")
  public static void alterAnnotationOnMethod(
      Method method, Class<? extends Annotation> annotationToAlter, Annotation annotationValue) {
    try {
      Field f = Executable.class.getDeclaredField(DECLARED_ANNOTATIONS);
      f.setAccessible(true);
      Map<Class<? extends Annotation>, Annotation> map =
          (Map<Class<? extends Annotation>, Annotation>) f.get(method);
      map.put(annotationToAlter, annotationValue);
    } catch (Exception e) {
      // ignore
    }
  }
}
