package org.openfast.examples;

import java.io.IOException;
import java.io.InputStream;

import org.openfast.Message;
import org.openfast.MessageBlockReader;
import org.openfast.examples.OpenFastExample.Variant;
import org.openfast.impl.CmeMessageBlockReader;
import org.openfast.impl.CmeTcpReplayMessageBlockReader;

public class MessageBlockReaderFactory {
	final Variant variant;
	final int offset;
    final boolean isMulticast;
		
	public MessageBlockReaderFactory() {
		this(Variant.DEFAULT, 0, true);
	}

	public MessageBlockReaderFactory(final Variant variant, final int offset, boolean isMulticast) {
		this.variant = variant;
		this.offset = offset;
        this.isMulticast = isMulticast;
	}

	public MessageBlockReader create() {
        if(Variant.CME == variant)
        {
            if(this.isMulticast)
                return new CmeMessageBlockReader();
            else
                return new CmeTcpReplayMessageBlockReader();
        }
        return createDefault();
    }

	MessageBlockReader createDefault() {
		if(offset <= 0)
			return MessageBlockReader.NULL;

		return new MessageBlockReader() {
			@Override
			public void messageRead(InputStream in, Message message) { }
			@Override
			public boolean readBlock(InputStream in) {
				try {
					in.skip(offset);
					return true;
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
			@Override public String toString() {
				return "(default block reader, offset=" + offset + ")";
			}
		};
	}
}
