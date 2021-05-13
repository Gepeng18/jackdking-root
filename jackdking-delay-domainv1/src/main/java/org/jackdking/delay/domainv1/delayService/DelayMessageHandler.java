package org.jackdking.delay.domainv1.delayService;

public interface DelayMessageHandler {
	
	public void handle(DelayMessage msg);
	
	public boolean match(DelayMessage msg);
	
	public boolean isAsyn();

}
