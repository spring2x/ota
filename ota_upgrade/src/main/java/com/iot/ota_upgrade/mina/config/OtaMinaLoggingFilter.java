package com.iot.ota_upgrade.mina.config;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.filter.logging.LoggingFilter;


/**
 * 重写mina的日志过滤器，减去部分日志，提高io效率
 * @author liqiang
 *
 */
public class OtaMinaLoggingFilter extends LoggingFilter {
	
	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		nextFilter.messageReceived(session, message);
	}
	
	@Override
	public void messageSent(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
		// TODO Auto-generated method stub
		nextFilter.messageSent(session, writeRequest);
	}
	
	@Override
	public void exceptionCaught(NextFilter nextFilter, IoSession session, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		nextFilter.exceptionCaught(session, cause);
	}
	
	
	@Override
	public void sessionCreated(NextFilter nextFilter, IoSession session) throws Exception {
		// TODO Auto-generated method stub
		nextFilter.sessionCreated(session);
	}
	
	@Override
	public void sessionOpened(NextFilter nextFilter, IoSession session) throws Exception {
		// TODO Auto-generated method stub
		nextFilter.sessionOpened(session);
	}
	
	@Override
	public void sessionIdle(NextFilter nextFilter, IoSession session, IdleStatus status) throws Exception {
		// TODO Auto-generated method stub
		nextFilter.sessionIdle(session, status);
	}
	
	@Override
	public void sessionClosed(NextFilter nextFilter, IoSession session) throws Exception {
		// TODO Auto-generated method stub
		 nextFilter.sessionClosed(session);
	}
}
