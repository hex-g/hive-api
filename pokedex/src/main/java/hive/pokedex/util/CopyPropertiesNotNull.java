package hive.pokedex.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public final class CopyPropertiesNotNull {

  public static String[] getNullAttributes (Object dest) {
    BeanWrapper source = new BeanWrapperImpl(dest);
    PropertyDescriptor[] attributes = source.getPropertyDescriptors();

    Set<String> emptyNames = new HashSet();
    for(PropertyDescriptor attribute : attributes) {
      Object attributeValue = source.getPropertyValue(attribute.getName());
      if (attributeValue != null && !attributeValue.toString().trim().isBlank()){
        emptyNames.add(attribute.getName());
      }
    }
    return emptyNames.toArray(new String[emptyNames.size()]);
  }

  public static void copyProperties(Object destiny, Object origin) {
    BeanUtils.copyProperties(origin, destiny, getNullAttributes(destiny));
  }

}
