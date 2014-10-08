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
package net.jr.ajp.client.impl.handlers;

import io.netty.buffer.ByteBuf;

import java.util.Collection;

import net.jr.ajp.client.Header;

public class AjpMessagesHandlerCbAdapter implements AjpMessagesHandlerCallback {

	@Override
	public void handleCPongMessage() throws Exception {
		// no-op
	}

	@Override
	public void handleEndResponseMessage(final boolean reuse) throws Exception {
		// no-op
	}

	@Override
	public void handleGetBodyChunkMessage(final int requestedLength) throws Exception {
		// no-op
	}

	@Override
	public void handleSendBodyChunkMessage(final ByteBuf data) throws Exception {
		// no-op
	}

	@Override
	public void handleSendHeadersMessage(final int statusCode, final String statusMessage, final Collection<Header> headers)
			throws Exception {
		// no-op
	}
}
