package org.openfast.template.type.codec;

import java.util.Date;

import org.openfast.DateValue;
import org.openfast.test.OpenFastTestCase;

public class EpochTimestampTest extends OpenFastTestCase {

	public void testEncodeDecode() {
		Date date = new Date(1201025733046L);
		assertEncodeDecode(new DateValue(date), "00100010 01111010 00010101 01011001 00100011 10110110", TypeCodec.EPOCH_TIMESTAMP);
	}
}
