/**
 * 
 */
package org.sinouplen.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Sinouplen
 * 
 * @param <T>
 * 
 */
public final class CastTrouble<T> {

	/**
	 * Hide Utility Class Constructor
	 */
	private CastTrouble() {

	}

	/**
	 * CastTrouble List of Objects without trouble casting List
	 * 
	 * @param <T>
	 * @param castingClass
	 * @param collection
	 * @return
	 */
	public static <T> List<T> castList(Class<? extends T> castingClass,
			Collection<?> collection) {
		List<T> listOfCastedObject = new ArrayList<T>(collection.size());
		for (Object objectToCast : collection) {
			listOfCastedObject.add(castingClass.cast(objectToCast));
		}
		return listOfCastedObject;
	}

	/**
	 * CastTrouble Object without trouble casting Object
	 * 
	 * @param <T>
	 * @param castingClass
	 * @param object
	 * @return
	 */
	public static <T> T cast(Class<? extends T> castingClass, Object object) {
		return castingClass.cast(object);
	}

	/**
	 * @param object
	 * @return
	 */
	public static <T> Class<T> getClass(T object) {
		return (Class<T>) object.getClass();
	}

	/**
	 * @param object
	 * @return
	 */
	public static <T> T cast(Object object) {
		return (T) object;
	}
}
