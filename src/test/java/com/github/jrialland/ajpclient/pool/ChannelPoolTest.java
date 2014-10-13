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

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

import com.github.jrialland.ajpclient.AbstractTomcatTest;

public class ChannelPoolTest extends AbstractTomcatTest {

	private static final int nTasks = 100;

	public ChannelPoolTest() {
		super(Protocol.Ajp, nTasks);
	}

	@Test
	public void testSimple() throws Exception {

		final Random random = new Random();

		Channels.getPool("localhost", getPort()).execute(new ChannelCallback() {

			@Override
			public void beforeUse(final Channel channel) {

			}

			@Override
			public void beforeRelease(final Channel channel) {

			}

			@Override
			public boolean __doWithChannel(final Channel channel) throws Exception {
				return random.nextBoolean();
			}

		});

	}

	@Test
	public void testMultiple() throws Exception {

		Channels.setMaxConnectionsPerHost(2);

		final Callable<Exception> task = new Callable<Exception>() {
			@Override
			public Exception call() throws Exception {
				try {
					testSimple();
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
			if (f.get() != null) {
				throw f.get();
			}
		}

	}
}
