package quick.pager.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ObjectUtils;
import quick.pager.job.common.utils.JobCenter;
import quick.pager.job.maintaince.model.JobModel;

import java.util.List;
import quick.pager.job.mapper.JobMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobApplicationTests {

	@Autowired
	private JobMapper jobMapper;
	@Autowired
	private Scheduler scheduler;

	@Test
	public void testJob(){
		List<JobModel> jobModelList = jobMapper.selectAll(new JobModel());

		try {
			for (JobModel model : jobModelList) {
				CronTrigger cronTrigger = JobCenter.getCronTrigger(scheduler, model.getJobName(), model.getJobGroup());
				if (ObjectUtils.isEmpty(cronTrigger)) {
					JobCenter.createJob(scheduler, model);
				} else {
					JobCenter.updateJob(scheduler, model);
				}
				System.out.println("sssssssssssssssssssssssssss"+scheduler.getTriggerState(TriggerKey.triggerKey(model.getJobName(),model.getJobGroup())));
			}
			System.out.println(JobCenter.getJobs(scheduler));
		}catch (Exception e){

		}


	}
}
