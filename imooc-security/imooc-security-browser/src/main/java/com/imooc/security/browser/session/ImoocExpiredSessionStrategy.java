/**
 *
 */
package com.imooc.security.browser.session;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Determines the behaviour of the ConcurrentSessionFilter when an expired session is detected in the ConcurrentSessionFilter.
 *
 * @author zhailiang
 */
public class ImoocExpiredSessionStrategy extends AbstractSessionStrategy implements SessionInformationExpiredStrategy {

	public ImoocExpiredSessionStrategy(String invalidSessionUrl) {
		super(invalidSessionUrl);
	}

	/**
	 * 登掉session后会触发什么操作
	 *
	 * @param event
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
		onSessionInvalid(event.getRequest(), event.getResponse());
	}

	@Override
	protected boolean isConcurrency() {
		return true;
	}
}
