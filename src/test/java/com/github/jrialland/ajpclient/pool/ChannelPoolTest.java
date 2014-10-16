/* Copyright (c) 2014 Julien Rialland <julien.rialland@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jrialland.ajpclient.pool;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

import com.github.jrialland.ajpclient.AbstractTomcatTest;
import com.github.jrialland.ajpclient.Forward;
import com.github.jrialland.ajpclient.impl.mock.MockForwardRequest;
import com.github.jrialland.ajpclient.impl.mock.MockForwardResponse;

public class ChannelPoolTest extends AbstractTomcatTest {

	private static final int nTasks = 100;

	private static final Path DIZZY_MP4 = Paths.get("./src/test/resources/dizzy.mp4");

	public ChannelPoolTest() {
		super(Protocol.Ajp, 1); // only one connector thread
		addStaticResource("/dizzy.mp4", DIZZY_MP4);
	}

	@Test
	public void testMultiple() throws Exception {

		final ChannelPool channelPool = new ChannelPool("localhost", getPort(), 1);

		final AtomicInteger counter = new AtomicInteger(0);

		final Callable<Exception> task = new Callable<Exception>() {
			@Override
			public Exception call() throws Exception {
				try {
					final MockForwardRequest request = new MockForwardRequest();
					final MockForwardResponse response = new MockForwardResponse();
					request.setRequestUri("/dizzy.mp4");
					channelPool.execute(new Forward(request, response), true);
					counter.incrementAndGet();
					return null;
				} catch (final Exception e) {
					return e;
				}
			}
		};

		final List<Callable<Exception>> tasks = new ArrayList<Callable<Exception>>();
		for (int i = 0; i < nTasks; i++) {
			tasks.add(task);
		}

		final List<Future<Exception>> futures = Executors.newCachedThreadPool().invokeAll(tasks);

		for (final Future<Exception> f : futures) {
			if (f.get(10, TimeUnit.SECONDS) != null) {
				throw f.get();
			}
		}

		Assert.assertEquals(nTasks, counter.get());
	}
}
