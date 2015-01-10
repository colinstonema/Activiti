/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.engine.impl.variable;

import java.util.Collection;

import org.activiti.engine.impl.util.CollectionUtil;

/**
 * 
 * @author colin
 *
 */
public class CollectionType implements VariableType {

  public String getTypeName() {
    return "collection";
  }

  public boolean isCachable() {
    return true;
  }

  public Object getValue(ValueFields valueFields) {
    if(valueFields.getLongCollectionValue() != null) {
      return valueFields.getLongCollectionValue();
    }
    if(valueFields.getStringCollectionValue() != null) {
      return valueFields.getStringCollectionValue();
    }
    if(valueFields.getDoubleCollectionValue() != null) {
      return valueFields.getDoubleCollectionValue();
    }
    return null;
  }

  public void setValue(Object value, ValueFields valueFields) {
    if(value == null) {
      return;
    }
    if(!(value instanceof Collection)) {
      return;
    }
    Collection collection = (Collection) value;
    if(collection.isEmpty()) {
      return;
    }

    Class clazz = CollectionUtil.getContentClass(collection);
    if(clazz == null) {
      return;
    }
    if(String.class.isAssignableFrom(clazz)) {
      valueFields.setStringCollectionValue(collection);
    } else if(Integer.class.isAssignableFrom(clazz) || int.class.isAssignableFrom(clazz)
        || Long.class.isAssignableFrom(clazz) || long.class.isAssignableFrom(clazz)
        || Short.class.isAssignableFrom(clazz)|| short.class.isAssignableFrom(clazz)
        || Byte.class.isAssignableFrom(clazz) || byte.class.isAssignableFrom(clazz)) {
      valueFields.setLongCollectionValue(collection);
    } else if(Double.class.isAssignableFrom(clazz) || double.class.isAssignableFrom(clazz)) {
      valueFields.setDoubleCollectionValue(collection);
    }
  }

  public boolean isAbleToStore(Object value) {
    if (value == null) {
      return true;
    }
    if(!Collection.class.isAssignableFrom(value.getClass())) {
      return false;
    }
    Collection collection = (Collection)value;
    Class clazz = CollectionUtil.getContentClass(collection);
    if(clazz == null) {
      return true;
    }
    if(String.class.isAssignableFrom(clazz)
        || Integer.class.isAssignableFrom(clazz) || int.class.isAssignableFrom(clazz)
        || Long.class.isAssignableFrom(clazz) || long.class.isAssignableFrom(clazz)
        || Short.class.isAssignableFrom(clazz)|| short.class.isAssignableFrom(clazz)
        || Byte.class.isAssignableFrom(clazz) || byte.class.isAssignableFrom(clazz)
        || Double.class.isAssignableFrom(clazz) || double.class.isAssignableFrom(clazz)) {
      return true;
    }
    return false;
  }
}