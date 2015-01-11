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

package org.activiti.engine.impl.cmd;

import java.io.Serializable;
import java.util.Date;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEventBuilder;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.JobEntity;
import org.activiti.engine.impl.persistence.entity.TimerEntity;


/**
 * @author colin
 */
public class ProgramTimerCmd implements Command<Void>, Serializable {

  private static final long serialVersionUID = 1L;

  private final String jobId;
  private final int retries;
  private final Date duedate;

  public ProgramTimerCmd(String jobId, int retries, Date duedate) {
    if (jobId == null || jobId.length() < 1) {
      throw new ActivitiIllegalArgumentException("The job id is mandatory, but '" + jobId + "' has been provided.");
    }
    if (retries < 0) {
      throw new ActivitiIllegalArgumentException("The number of job retries must be a non-negative Integer, but '" + retries + "' has been provided.");
    }
    this.jobId = jobId;
    this.retries = retries;
    this.duedate = duedate;
  }

  public Void execute(CommandContext commandContext) {
    JobEntity job = commandContext
            .getJobEntityManager()
            .findJobById(jobId);

    if (job != null && job instanceof TimerEntity) {
      boolean needProgram = false;
      if(retries != 0) {
        if(job.getRetries() != retries) {
          job.setRetries(retries);
          needProgram = true;
        }
      }
      if(duedate != null) {
        if(job.getDuedate() != null) {
          if(job.getDuedate().getTime() != duedate.getTime()) {
            job.setDuedate(duedate);
            needProgram = true;
          }
        }
      }
      if(needProgram) {
        commandContext.getJobEntityManager().program((TimerEntity)job);
        if(commandContext.getEventDispatcher().isEnabled()) {
          commandContext.getEventDispatcher().dispatchEvent(
              ActivitiEventBuilder.createEntityEvent(ActivitiEventType.ENTITY_UPDATED, job));
        }
      }
    } else {
      throw new ActivitiObjectNotFoundException("No timer found with id '" + jobId + "'.", TimerEntity.class);
    }
    return null;
  }
}
