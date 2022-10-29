package org.egov.ifix.scheduler;

import java.util.List;

import javax.transaction.Transactional;

import org.egov.ifix.consumer.EventTypeConsumer;
import org.egov.ifix.persistance.EventPostingDetail;
import org.egov.ifix.persistance.EventPostingDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {
	
	@Autowired
	EventPostingDetailRepository eventPostingDetailRepository;
	@Autowired
	private EventTypeConsumer consumer;
	
	@Transactional
	@Scheduled (cron="${firetime}")
	public void readAndPush()
	{
		
		List<EventPostingDetail> notPostedList = eventPostingDetailRepository.findByStatus("500");
		
		for(EventPostingDetail detail:notPostedList)
		{
			consumer.process(detail.getRecord(),detail);
		}
		
	}
	
}
