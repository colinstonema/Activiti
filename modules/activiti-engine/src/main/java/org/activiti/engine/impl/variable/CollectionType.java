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

import org.apache.commons.lang3.StringUtils;



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
    return valueFields.getTextValue();
  }

  public void setValue(Object value, ValueFields valueFields) {
    if(!(value instanceof Collection)) {
      return;
    }
    Collection collection = (Collection) value;
    String separator = null;
    if(collection.size() != 0) {
      Object obj = collection.iterator().next();
      if (obj instanceof Number) {
        separator = ",";
      } else {
        separator = "','";
      }
    }
    String textValue = StringUtils.join(collection, separator);
    valueFields.setTextValue(textValue);
  }

  public boolean isAbleToStore(Object value) {
    if (value==null) {
      return true;
    }
    if(Collection.class.isAssignableFrom(value.getClass())) {
      Collection collection = (Collection) value;
      if(collection.isEmpty()) {
        return true;
      }
      Object obj = collection.iterator().next();
      if(String.class.isAssignableFrom(obj.getClass())) {
        return true;
      } else if(Number.class.isAssignableFrom(obj.getClass())) {
        return true;
      } else {
        return false;
      }
    }
    return false;
  }
}
