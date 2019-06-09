package com.diageo.mras.webservices.modals;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

@Provider
public class MapMessageWriter implements
		MessageBodyWriter<HashMap<String, String>> {

	@Override
	public long getSize(HashMap<String, String> t, Class<?> type,
			Type genericType, Annotation[] annotations, MediaType mediaType) {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {

		return mediaType.isCompatible(MediaType.APPLICATION_XML_TYPE);
	}

	@Override
	public void writeTo(HashMap<String, String> t, Class<?> type,
			Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {

		// Simple marshalling
		StringBuffer sb = new StringBuffer("<map>");
		for (Map.Entry<String, String> entry : t.entrySet()) {
			sb.append("<entry>");
			sb.append(" <key>").append(entry.getKey()).append("</key>");
			sb.append(" <value>").append(entry.getValue()).append("</value>");
			sb.append("</entry>");
		}
		sb.append("</map>");

		entityStream.write(sb.toString().getBytes());
	}

}
