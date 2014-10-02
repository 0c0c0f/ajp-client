java client library for the Apache JServ Protocol 1.3
==============================

This is a java implementation of an ajp13 client, allowing to send requests to a servlet container using this protocol.

This library relies on netty 4.0 and commons-pool2

Licensed under the Apache License, Version 2.0 (see [LICENSE](https://github.com/jrialland/ajp-client/blob/master/LICENSE))

Simple Usecases :
------------------

* Making a cPing request

```java
	import net.jr.ajp.client.pool.Channels;
	import net.jr.ajp.client.CPing;

	...

	//get a tcp connection
	final Channel channel = Channels.connect("localhost", 8009);
	//will try a cping/cpong exchange on the opened tcp connection
	boolean success = new CPing(2, TimeUnit.SECONDS).doWithChannel(channel);
```

* Making a forward request

```java
	import net.jr.ajp.client.pool.Channels;
	import net.jr.ajp.client.Forward;

	//get a tcp connection
	final Channel channel = Channels.connect("localhost", 8009);
	//send a forward request
	new Forward(ajpRequest, ajpResponse).doWithChannel(channel);
	
```

* Using a client sockets pool :

Socket pools handle the creation and destruction of multiple connections automatically.

```java
	import net.jr.ajp.client.pool.Channels;
	import net.jr.ajp.client.Forward;
	
	Channels.getPool("localhost", 8009).execute(new Forward(ajpRequest, ajpResponse));
	
```
Will use a socket channel picked from a pool, allowing the reuse of sockets among request.

* The library can be used directly in a servlet container in order to forward requests to another servlet container :

```java
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;
	import net.jr.ajp.client.servlet.AjpServletProxy;
	
	HttpServletRequest request = ...
	HttpServetResponse response = ...
	new AjpServletProxy.forHost("localhost", 8009).forward(request, response);
```