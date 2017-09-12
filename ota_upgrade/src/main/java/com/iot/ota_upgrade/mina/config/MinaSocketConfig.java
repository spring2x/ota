package com.iot.ota_upgrade.mina.config;

import java.net.InetSocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.iot.ota_upgrade.mina.code.CodeFactory;
import com.iot.ota_upgrade.mina.handler.DeviceUpgradeHandler;

@Configuration
@ConfigurationProperties(prefix="mina")
public class MinaSocketConfig {

	/**
	 * 监听端口
	 */
	private int port;
	
	/**
	 * 绑定ip
	 */
	private String ip;
	
	
	@Bean(initMethod = "bind", destroyMethod = "unbind")
	public NioSocketAcceptor nioSocketAcceptor(DeviceUpgradeHandler deviceUpgradeHandler,
			DefaultIoFilterChainBuilder defaultIoFilterChainBuilder) {

		NioSocketAcceptor nioSocketAcceptor = new NioSocketAcceptor();
		nioSocketAcceptor.setDefaultLocalAddress(new InetSocketAddress(ip, port));
		nioSocketAcceptor.setReuseAddress(true);
		nioSocketAcceptor.setFilterChainBuilder(defaultIoFilterChainBuilder);
		nioSocketAcceptor.setHandler(deviceUpgradeHandler);
		return nioSocketAcceptor;
	}
	
	
	@Bean
    public DefaultIoFilterChainBuilder defaultIoFilterChainBuilder(ExecutorFilter executorFilter, MdcInjectionFilter mdcInjectionFilter, ProtocolCodecFilter protocolCodecFilter, LoggingFilter loggingFilter) {
        DefaultIoFilterChainBuilder defaultIoFilterChainBuilder = new DefaultIoFilterChainBuilder();
        Map<String, IoFilter> filters = new LinkedHashMap<>();
        filters.put("executor", executorFilter);
        filters.put("mdcInjectionFilter", mdcInjectionFilter);
        filters.put("codecFilter", protocolCodecFilter);
        filters.put("loggingFilter", loggingFilter);
        defaultIoFilterChainBuilder.setFilters(filters);
        return defaultIoFilterChainBuilder;
    }
	
	@Bean
    public ExecutorFilter executorFilter() {
        return new ExecutorFilter();
    }
	
	@Bean
	public MdcInjectionFilter mdcInjectionFilter() {
		return new MdcInjectionFilter(MdcInjectionFilter.MdcKey.remoteAddress);
	}
	
	@Bean
    public LoggingFilter loggingFilter() {
        return new OtaMinaLoggingFilter();
    }
	
	@Bean
    public ProtocolCodecFilter protocolCodecFilter(CodeFactory minaCodeFactory) {
        return new ProtocolCodecFilter(minaCodeFactory);
    }
	
	@Bean
	public CodeFactory codeFactory(){
		return new CodeFactory();
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
	
}
