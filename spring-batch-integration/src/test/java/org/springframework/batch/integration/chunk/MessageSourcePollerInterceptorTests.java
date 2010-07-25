package org.springframework.batch.integration.chunk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.integration.channel.ThreadLocalChannel;
import org.springframework.integration.core.Message;
import org.springframework.integration.message.GenericMessage;
import org.springframework.integration.message.MessageSource;

public class MessageSourcePollerInterceptorTests {

	@Test(expected = IllegalStateException.class)
	public void testMandatoryPropertiesUnset() throws Exception {
		MessageSourcePollerInterceptor interceptor = new MessageSourcePollerInterceptor();
		interceptor.afterPropertiesSet();
	}

	@Test
	public void testMandatoryPropertiesSetViaConstructor() throws Exception {
		MessageSourcePollerInterceptor interceptor = new MessageSourcePollerInterceptor(new TestMessageSource("foo"));
		interceptor.afterPropertiesSet();
	}

	@Test
	public void testMandatoryPropertiesSet() throws Exception {
		MessageSourcePollerInterceptor interceptor = new MessageSourcePollerInterceptor();
		interceptor.setMessageSource(new TestMessageSource("foo"));
		interceptor.afterPropertiesSet();
	}

	@Test
	public void testPreReceive() throws Exception {
		MessageSourcePollerInterceptor interceptor = new MessageSourcePollerInterceptor(new TestMessageSource("foo"));
		ThreadLocalChannel channel = new ThreadLocalChannel();
		assertTrue(interceptor.preReceive(channel));
		assertEquals("foo", channel.receive(10L).getPayload());
	}

	private static class TestMessageSource implements MessageSource<String> {

		private String payload;

		public TestMessageSource(String payload) {
			super();
			this.payload = payload;
		}

		public Message<String> receive() {
			return new GenericMessage<String>(payload);
		}
	}

}