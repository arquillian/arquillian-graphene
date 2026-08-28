package org.arquillian.graphene.rusheye.enricher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.List;

import org.arquillian.graphene.rusheye.annotation.RushEye;
import org.arquillian.graphene.rusheye.annotation.Snap;
import org.arquillian.graphene.rusheye.comparator.OcularImpl;
import org.arquillian.graphene.rusheye.comparator.SnapshotAttributes;
import org.arquillian.graphene.rusheye.exception.RushEyeExtensionInitializationException;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.test.spi.TestEnricher;



public class RushEyeEnricher implements TestEnricher {

    public void enrich(Object page) {

    	if(page.getClass().isAnnotationPresent(Snap.class)){
            
    		boolean isPageFragment = this.isRootPresent(page);
    		
            String pattern = page.getClass().getAnnotation(Snap.class).value();            
            float onePixelTreshold = page.getClass().getAnnotation(Snap.class).onePixelThreshold();
            int similarityCutOff = page.getClass().getAnnotation(Snap.class).similarityCutOff();
            String[] masks = page.getClass().getAnnotation(Snap.class).masks();
            
            final SnapshotAttributes snapshotAttributes = new SnapshotAttributes(pattern, onePixelTreshold, similarityCutOff, masks);
            List<Field> fields = getFieldsWithAnnotation(page.getClass(), RushEye.class);
            for (Field field : fields) {
                try {
                    setFieldValue(page, field, new OcularImpl(isPageFragment, snapshotAttributes));
                } catch (Exception e) {
                    throw new RushEyeExtensionInitializationException("Could not inject RushEye on field " + field, e);
                }
            }
    	}
    	
    }


    public Object[] resolve(Method method) {
    	throw new RuntimeException("RushEye can not be injected on a method");
    }
    
    /**
     * Checks for the Root annotation 
     *
     * @param instance - the object who might have Root annotation
     * @return true if Root is found; false otherwise
     */  
    private boolean isRootPresent(Object page){
    	List<Field> fields = getFieldsWithAnnotation(page.getClass(), Root.class);
    	return !fields.isEmpty();
    }

    /**
     * Sets the field represented by Field object on the specified object argument to the specified new value. The new value is
     * automatically unwrapped if the underlying field has a primitive type.
     *
     * @param instance - the object whose field should be modified
     * @param field - the field that should be modified
     * @param value - the new value for the field of obj being modified
     */
    private void setFieldValue(final Object instance, final Field field, final Object value) {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
                public Void run() throws IllegalArgumentException, IllegalAccessException {
                    field.set(instance, value);
                    return null;
                }
            });
        } catch (PrivilegedActionException e) {
            final Throwable t = e.getCause();
            // Rethrow
            if (t instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) t;
            } else if (t instanceof IllegalAccessException) {
                throw new IllegalStateException("Unable to set field value of " + field.getName() + " due to: "
                    + t.getMessage(), t.getCause());
            } else {
                // No other checked Exception thrown by Class.getConstructor
                try {
                    throw (RuntimeException) t;
                }
                // Just in case we've really messed up
                catch (final ClassCastException cce) {
                    throw new RushEyeExtensionInitializationException("Obtained unchecked Exception; this code should never be reached", t);
                }
            }
        }
    }

    /**
     * Returns all the fields annotated with the given annotation
     *
     * @param source - the class where the fields should be examined in
     * @param annotationClass - the annotation the fields should be annotated with
     * @return list of found fields annotated with the given annotation
     */
    private List<Field> getFieldsWithAnnotation(final Class<?> source, final Class<? extends Annotation> annotationClass) {
        List<Field> declaredAccessableFields = AccessController.doPrivileged(new PrivilegedAction<List<Field>>() {

            public List<Field> run() {
                List<Field> foundFields = new ArrayList<Field>();
                Class<?> nextSource = source;

                while (nextSource != Object.class) {
                    for (Field field : nextSource.getDeclaredFields()) {

                        if (field.isAnnotationPresent(annotationClass)) {
                            if (!field.isAccessible()) {
                                field.setAccessible(true);
                            }
                            foundFields.add(field);
                        }
                    }
                    nextSource = nextSource.getSuperclass();
                }
                return foundFields;
            }
        });
        return declaredAccessableFields;
    }

}