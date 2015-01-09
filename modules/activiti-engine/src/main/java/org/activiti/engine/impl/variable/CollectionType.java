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

/**
 * 
 * @author colin
 *
 */
public class CollectionType implements VariableType {

  public String getTypeName() {
    return "null";
  }

  public boolean isCachable() {
    return true;
  }

  public Object getValue(ValueFields valueFields) {
    return valueFields.getNumberCollectionValue() == null ? valueFields.getStringCollectionValue() : valueFields.getNumberCollectionValue();
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
    Object obj = collection.iterator().next();
    if(String.class.isAssignableFrom(obj.getClass())) {
      valueFields.setStringCollectionValue(collection);
    } else if(Number.class.isAssignableFrom(obj.getClass())) {
      valueFields.setNumberCollectionValue(collection);
    }
  }

  public boolean isAbleToStore(Object value) {
    return false;
  }
}
