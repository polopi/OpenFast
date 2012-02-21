package org.openfast.util;

import junit.framework.TestCase;

public class ArrayIteratorTest extends TestCase {

	public void testNext() {
		String[] values = new String[] { "a", "b", "c" };
		ArrayIterator<String> iter = new ArrayIterator<String>(values);
		assertTrue(iter.hasNext());
		assertEquals(values[0], iter.next());
		assertTrue(iter.hasNext());
		assertEquals(values[1], iter.next());
		assertTrue(iter.hasNext());
		assertEquals(values[2], iter.next());
		assertFalse(iter.hasNext());
		
	}

	public void testRemove() {
		try {
			new ArrayIterator<String>(new String[] { "a" }).remove();
			fail();
		} catch (UnsupportedOperationException e) {
			
		}
	}
}
