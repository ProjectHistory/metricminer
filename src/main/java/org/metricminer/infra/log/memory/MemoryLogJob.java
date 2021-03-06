package org.metricminer.infra.log.memory;

import java.util.Calendar;

import org.apache.log4j.Logger;

import br.com.caelum.vraptor.ioc.PrototypeScoped;
import br.com.caelum.vraptor.tasks.Task;
import br.com.caelum.vraptor.tasks.scheduler.Scheduled;

@PrototypeScoped
@Scheduled(cron = "0 0/10 0-23 * * ?")
public class MemoryLogJob implements Task {

	private static Logger log = Logger.getLogger(MemoryLogJob.class);
	
	@Override
	public void execute() {
		Runtime runtime = Runtime.getRuntime();  
		
		long maxMemory = runtime.maxMemory();  
		long allocatedMemory = runtime.totalMemory();  
		long freeMemory = runtime.freeMemory();  
		
		log.debug("------- " + Calendar.getInstance().getTime());
		log.debug("free memory: " + freeMemory / 1024);  
		log.debug("allocated memory: " + allocatedMemory / 1024);  
		log.debug("max memory: " + maxMemory /1024);  
		log.debug("total free memory: " +   
				(freeMemory + (maxMemory - allocatedMemory)) / 1024);
	}
}
