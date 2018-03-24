package org.openfast.scenario;

import org.openfast.Message;
import org.openfast.MessageInputStream;
import org.openfast.template.loader.XMLMessageTemplateLoader;
import org.openfast.test.OpenFastTestCase;

import java.io.InputStream;

public class EurexTemplateTest extends OpenFastTestCase {
	public void testDeltas() throws Exception {
		InputStream templateSource = resource("EUREX/RDDFastTemplates-1.1.xml");
		XMLMessageTemplateLoader templateLoader = new XMLMessageTemplateLoader();
		templateLoader.setLoadTemplateIdFromAuxId(true);
		templateLoader.load(templateSource);
		
		InputStream is = resource("EUREX/messages.fast");
		MessageInputStream mis = new MessageInputStream(is);
		mis.setTemplateRegistry(templateLoader.getTemplateRegistry());
		mis.readMessage();
		Message md = mis.readMessage();
		assertEquals(-5025.0, md.getSequence("MDEntries").get(0).getDouble("NetChgPrevDay"), .1);
	}
}
